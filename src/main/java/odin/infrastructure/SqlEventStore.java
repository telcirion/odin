/* Copyright 2019 Peter Jansen
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

import odin.concepts.applicationservices.IRepository;
import odin.concepts.common.ISendMessage;
import odin.concepts.domainmodel.IAggregateRoot;
import odin.concepts.domainmodel.IDomainEvent;
import odin.concepts.domainmodel.ISendDomainEvent;
import odin.concepts.infra.IDataSource;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

public class SqlEventStore<T> implements IRepository<T>, ISendDomainEvent {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final IDataSource ds;
    private final ISendMessage eventBus;

    public SqlEventStore(IDataSource ds, ISendMessage eventBus) {
        this.eventBus = eventBus;
        this.ds = ds;
    }

    private void executeSqlUpdate(String sqlString) {
        Statement statement = null;
        try {
            statement = ds.getConnection().createStatement();
            statement.executeUpdate(sqlString);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());

        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    public void createDatabase() {
        String createSchema = "CREATE SCHEMA IF NOT EXISTS EVENT_STORE;\r\n"
                + "DROP TABLE IF EXISTS EVENT_STORE.EVENT CASCADE;\r\n"
                + "CREATE TABLE IF NOT EXISTS  EVENT_STORE.EVENT(ID UUID PRIMARY KEY, "
                + "AGGREGATE_ID UUID NOT NULL , TIMESTAMP TIMESTAMP, CLASSNAME VARCHAR(255), DATA TEXT);";
        executeSqlUpdate(createSchema);
    }

    private String eventInserts(IDomainEvent s) {
        StringBuilder b = new StringBuilder();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.create();
        b.append("INSERT INTO EVENT_STORE.EVENT (ID, AGGREGATE_ID, TIMESTAMP, CLASSNAME, DATA) VALUES ('")
                .append(s.getEventId()).append("','").append(s.getAggregateId()).append("','").append(s.getTimestamp())
                .append("','").append(s.getClass().getName()).append("','").append(gson.toJson(s)).append("');\r\n");
        return b.toString();
    }

    @Override
    public T get(IAggregateRoot<T> aggregate) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();
        ArrayList<IAggregateRoot<T>> l = new ArrayList<>();
        l.add(aggregate);
        executeSqlQuery("SELECT CLASSNAME, DATA FROM EVENT_STORE.EVENT WHERE AGGREGATE_ID='" + aggregate.getId()
                + "' ORDER BY TIMESTAMP;", resultSet -> {
                    try {
                        l.add(l.get(l.size() - 1).dispatch(
                                (IDomainEvent) gson.fromJson(resultSet.get(1), Class.forName(resultSet.get(0)))));
                    } catch (JsonSyntaxException | ClassNotFoundException ex) {
                        logger.error(ex.getMessage());
                    }
                });
        return l.get(l.size() - 1).getSnapshot();
    }

    class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime));
        }
    }

    class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    @Override
    public void send(IDomainEvent event) {
        executeSqlUpdate(eventInserts(event));
        eventBus.send(event);
    }

    @FunctionalInterface
    public interface IRowAction {
        void executeAction(List<String> msg);
    }

    private void executeSqlQuery(String query, IRowAction rowAction) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = ds.getConnection().prepareStatement(query);
            resultSet = statement.executeQuery();
            ResultSetMetaData rsMetaData = resultSet.getMetaData();
            while (resultSet.next()) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
                    list.add(resultSet.getString(i + 1));
                }
                rowAction.executeAction(list);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    public void dump() {
        executeSqlQuery("SELECT AGGREGATE_ID, TIMESTAMP, CLASSNAME, DATA FROM EVENT_STORE.EVENT "
                + "ORDER BY AGGREGATE_ID,TIMESTAMP;", resultSet -> {
                    String row = String.format("%s %s %s %s%n", resultSet.get(0), resultSet.get(1), resultSet.get(2),
                            resultSet.get(3));
                    logger.info(row);
                });
    }

}
