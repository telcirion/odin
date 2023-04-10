/* Copyright 2020 Peter Jansen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package odin.framework.infrastructure;

import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import odin.concepts.applicationservices.EventStore;
import odin.concepts.common.Identity;
import odin.concepts.domainmodel.DomainEvent;
import odin.concepts.infra.DataSource;

public class SqlEventStore implements EventStore {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DataSource ds;

    public SqlEventStore(final DataSource ds) {
        this.ds = ds;
    }

    @Override
    public void save(DomainEvent event) {

        ObjectMapper o = new ObjectMapper();
        o.registerModule(new JavaTimeModule());
        try (PreparedStatement statement = ds.getConnection().prepareStatement(
                "INSERT INTO EVENT_STORE.EVENT (ID, AGGREGATE_ID, TIMESTAMP, CLASSNAME, DATA) VALUES(?, ?, ?, ?, ?)")) {
            statement.setString(1, event.getMessageInfo().messageId().toString());
            statement.setString(2, event.getMessageInfo().subjectId().toString());
            statement.setString(3, event.getMessageInfo().timestamp().toString());
            statement.setString(4, event.getClass().getName());
            statement.setString(5, o.writeValueAsString(event));
            statement.executeUpdate();
        } catch (SQLException | JsonProcessingException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public List<DomainEvent> load(Identity id) {
        final ArrayList<DomainEvent> eventList = new ArrayList<>();
        try (PreparedStatement statement = ds.getConnection().prepareStatement(
                "SELECT CLASSNAME, DATA FROM EVENT_STORE.EVENT WHERE AGGREGATE_ID=? ORDER BY TIMESTAMP;")) {
            statement.setString(1, id.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.registerModule(new ParameterNamesModule());
                while (resultSet.next()) {
                    eventList.add((DomainEvent) mapper.readValue(resultSet.getString(2),
                            Class.forName(resultSet.getString(1))));
                }
                return eventList;
            }
        } catch (SQLException | ClassNotFoundException | JsonProcessingException ex) {
            logger.error(ex.getMessage());
            return new ArrayList<>();
        }
    }

    public void createDatabase() {
        try (PreparedStatement statement = ds.getConnection()
                .prepareStatement("""
                        CREATE SCHEMA IF NOT EXISTS EVENT_STORE;
                        DROP TABLE IF EXISTS EVENT_STORE.EVENT CASCADE;
                        CREATE TABLE IF NOT EXISTS  EVENT_STORE.EVENT(ID UUID PRIMARY KEY,
                        AGGREGATE_ID UUID NOT NULL , TIMESTAMP TIMESTAMP,
                        CLASSNAME VARCHAR(255), DATA TEXT);""")) {
            statement.executeUpdate();
        } catch (final SQLException ex) {
            logger.error(ex.getMessage());
        }

    }
}
