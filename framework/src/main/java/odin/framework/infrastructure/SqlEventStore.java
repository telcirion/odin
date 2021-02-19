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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.concepts.applicationservices.IEventStore;
import odin.concepts.common.Identity;
import odin.concepts.domainmodel.IDomainEvent;
import odin.concepts.infra.IDataSource;

public class SqlEventStore implements IEventStore {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final IDataSource ds;

    public SqlEventStore(final IDataSource ds) {
        this.ds = ds;
    }

    @Override
    public void save(IDomainEvent event) {
        executeSqlUpdate(generateInsert(event));
    }

    @Override
    public List<IDomainEvent> load(Identity id) {
        final ArrayList<IDomainEvent> eventList = new ArrayList<>();
        try (PreparedStatement statement = ds.getConnection().prepareStatement(
                "SELECT CLASSNAME, DATA FROM EVENT_STORE.EVENT WHERE AGGREGATE_ID=? ORDER BY TIMESTAMP;")) {
            statement.setString(1, id.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                ObjectMapper o = new ObjectMapper();
                o.registerModule(new JavaTimeModule());
                while (resultSet.next()) {
                    eventList.add(
                            (IDomainEvent) o.readValue(resultSet.getString(2), Class.forName(resultSet.getString(1))));
                }
                return eventList;
            }
        } catch (SQLException | ClassNotFoundException | JsonProcessingException ex) {
            logger.error(ex.getMessage());
            return new ArrayList<>();
        }
    }

    public void createDatabase() {
        final String createSchema = "CREATE SCHEMA IF NOT EXISTS EVENT_STORE;\r\n"
                + "DROP TABLE IF EXISTS EVENT_STORE.EVENT CASCADE;\r\n"
                + "CREATE TABLE IF NOT EXISTS  EVENT_STORE.EVENT(ID UUID PRIMARY KEY, "
                + "AGGREGATE_ID CHAR(36) NOT NULL , TIMESTAMP TIMESTAMP, CLASSNAME VARCHAR(255), DATA TEXT);";
        executeSqlUpdate(createSchema);
    }

    private void executeSqlUpdate(final String sqlString) {
        try (Statement statement = ds.getConnection().createStatement()) {
            statement.executeUpdate(sqlString);
        } catch (final SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    private String generateInsert(final IDomainEvent s) {
        final StringBuilder b = new StringBuilder();
        ObjectMapper o = new ObjectMapper();
        o.registerModule(new JavaTimeModule());
        try {
            b.append("INSERT INTO EVENT_STORE.EVENT (ID, AGGREGATE_ID, TIMESTAMP, CLASSNAME, DATA) VALUES ('")
                    .append(s.getMessageInfo().getMessageId()).append("','")
                    .append(s.getMessageInfo().getSubjectId().toString()).append("','")
                    .append(s.getMessageInfo().getTimestamp()).append("','").append(s.getClass().getName())
                    .append("','").append(o.writeValueAsString(s)).append("');\r\n");
        } catch (JsonProcessingException ex) {
            logger.error(ex.getMessage());
        }
        return b.toString();
    }

}
