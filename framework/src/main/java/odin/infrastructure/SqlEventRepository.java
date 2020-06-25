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

package odin.infrastructure;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.concepts.applicationservices.IRepository;
import odin.concepts.common.ISendMessage;
import odin.concepts.domainmodel.IAggregate;
import odin.concepts.domainmodel.IAggregateRoot;
import odin.concepts.domainmodel.IDomainEvent;
import odin.concepts.infra.IDataSource;

public class SqlEventRepository implements IRepository {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final IDataSource ds;
    private final ISendMessage eventBus;

    public SqlEventRepository(final IDataSource ds, final ISendMessage eventBus) {
        this.eventBus = eventBus;
        this.ds = ds;
    }

    private void executeSqlUpdate(final String sqlString) {
        Statement statement = null;
        try {
            statement = ds.getConnection().createStatement();
            statement.executeUpdate(sqlString);
        } catch (final SQLException ex) {
            logger.error(ex.getMessage());

        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (final SQLException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    public void createDatabase() {
        final String createSchema = "CREATE SCHEMA IF NOT EXISTS EVENT_STORE;\r\n"
                + "DROP TABLE IF EXISTS EVENT_STORE.EVENT CASCADE;\r\n"
                + "CREATE TABLE IF NOT EXISTS  EVENT_STORE.EVENT(ID UUID PRIMARY KEY, "
                + "AGGREGATE_ID CHAR(36) NOT NULL , TIMESTAMP TIMESTAMP, CLASSNAME VARCHAR(255), DATA TEXT);";
        executeSqlUpdate(createSchema);
    }

    private String generateInsert(final IDomainEvent s) {
        final StringBuilder b = new StringBuilder();
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        final Gson gson = gsonBuilder.create();
        b.append("INSERT INTO EVENT_STORE.EVENT (ID, AGGREGATE_ID, TIMESTAMP, CLASSNAME, DATA) VALUES ('")
                .append(s.getMessageInfo().getMessageId()).append("','")
                .append(s.getMessageInfo().getSubjectId().toString()).append("','")
                .append(s.getMessageInfo().getTimestamp()).append("','").append(s.getClass().getName())
                .append("','").append(gson.toJson(s)).append("');\r\n");
        return b.toString();
    }

    @Override
    public <K extends IAggregate<? extends IAggregateRoot>> K load(K aggregate) {
        final List<IDomainEvent> resultSet = getEventList(aggregate);
        resultSet.forEach(aggregate::source);
        return aggregate;
    }

    private List<IDomainEvent> getEventList(final IAggregate<?> aggregate) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        final ArrayList<IDomainEvent> eventList = new ArrayList<>();
        try {
            statement = ds.getConnection().prepareStatement(
                    "SELECT CLASSNAME, DATA FROM EVENT_STORE.EVENT WHERE AGGREGATE_ID=? ORDER BY TIMESTAMP;");
            statement.setString(1, aggregate.getId().toString());
            resultSet = statement.executeQuery();
            final GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
            final Gson gson = gsonBuilder.create();
            while (resultSet.next()) {
                eventList.add(
                        (IDomainEvent) gson.fromJson(resultSet.getString(2), Class.forName(resultSet.getString(1))));
            }
            return eventList;
        } catch (SQLException | JsonSyntaxException | ClassNotFoundException ex) {
            logger.error(ex.getMessage());
            return new ArrayList<>();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (final SQLException ex) {
                logger.error(ex.getMessage());
            }
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (final SQLException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
        @Override
        public JsonElement serialize(final LocalDateTime localDateTime, final Type srcType,
                final JsonSerializationContext context) {
            return new JsonPrimitive(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime));
        }
    }

    class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(final JsonElement json, final Type typeOfT,
                final JsonDeserializationContext context) {
            return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    @Override
    public <K extends IAggregate<? extends IAggregateRoot>> void save(K obj) {
        obj.getAddedEvents().forEach(e -> {
            executeSqlUpdate(generateInsert(e));
            eventBus.send(e);
        });
    }
}
