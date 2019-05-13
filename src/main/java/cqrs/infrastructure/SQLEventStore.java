// Copyright 2019 Peter Jansen
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package cqrs.infrastructure;

import com.google.gson.*;
import cqrs.concepts.applicationservices.IRepository;
import cqrs.concepts.domainmodel.IAggregateRoot;
import cqrs.concepts.domainmodel.IDomainEvent;
import cqrs.concepts.infra.IDataSource;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SQLEventStore<T extends IAggregateRoot<T>> implements IRepository<T> {

	private final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final IDataSource ds;
	
	public SQLEventStore(IDataSource ds) {
		this.ds=ds;
	}

	public void createDatabase() {
		
		try {
			Statement statement = ds.getConnection().createStatement();
			String schemaDDL = "CREATE SCHEMA IF NOT EXISTS EVENT_STORE;\r\n"
					+ "DROP TABLE IF EXISTS EVENT_STORE.AGGREGATE;\r\n"
					+ "CREATE TABLE IF NOT EXISTS  EVENT_STORE.AGGREGATE(ID UUID PRIMARY KEY, CLASSNAME VARCHAR(255));\r\n"
					+ "DROP TABLE IF EXISTS EVENT_STORE.EVENT;\r\n"
					+ "CREATE TABLE IF NOT EXISTS  EVENT_STORE.EVENT(ID UUID PRIMARY KEY,  AGGREGATE_ID UUID NOT NULL , TIMESTAMP TIMESTAMP, CLASSNAME VARCHAR(255), DATA VARCHAR(MAX),"
					+ "FOREIGN KEY (AGGREGATE_ID) REFERENCES EVENT_STORE.AGGREGATE(ID));";
			statement.executeUpdate(schemaDDL);
		} catch (SQLException ex) {
			logger.error(ex.getMessage());
		}
	}

	@Override
	public void create(IAggregateRoot<T> obj) {
		String sqlStmt = "INSERT INTO EVENT_STORE.AGGREGATE (ID, CLASSNAME) VALUES ('"
				+ obj.getId() +"','" + obj.getClass().getName() + "');";
		sqlStmt=sqlStmt+eventInserts(obj);
		try {
			Statement statement = ds.getConnection().createStatement();
			statement.executeUpdate(sqlStmt);
		} catch (SQLException ex) {
			logger.error(ex.getMessage());
			throw new RuntimeException(ex);
		}
	}

	private String eventInserts(IAggregateRoot<T> obj) {
		StringBuilder b=new StringBuilder();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
		Gson gson = gsonBuilder.create();
		obj.getEvents().forEach(s-> b.append("INSERT INTO EVENT_STORE.EVENT (ID, AGGREGATE_ID, TIMESTAMP, CLASSNAME, DATA) VALUES ('").append(s.getEventId()).append("','").append(obj.getId()).append("','").append(s.getTimestamp()).append("','").append(s.getClass().getName()).append("','").append(gson.toJson(s)).append("');\r\n"));
		return b.toString();
	}
	
	@Override
	public void update(IAggregateRoot<T> obj) {
		
		try {
			Statement statement = ds.getConnection().createStatement();
			statement.executeUpdate(eventInserts(obj));
		} catch (SQLException ex) {
			logger.error(ex.getMessage());
			throw new RuntimeException(ex);
		}
	}

	@Override
	public T get(IAggregateRoot<T> aggregate) {

		try {
			Statement statement = ds.getConnection().createStatement();
			ResultSet a=statement.executeQuery("SELECT CLASSNAME, DATA FROM EVENT_STORE.EVENT WHERE AGGREGATE_ID='"+aggregate.getId()+"' ORDER BY TIMESTAMP;");
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
			Gson gson = gsonBuilder.create();			while (a.next()) {
				aggregate= aggregate.applyEvent((IDomainEvent) gson.fromJson(a.getString(2), Class.forName(a.getString(1))));
			}
			return aggregate.getSnapshot();
		} catch (SQLException | JsonSyntaxException | ClassNotFoundException ex) {
			logger.error(ex.getMessage());
			throw  new RuntimeException(ex);
		}
	}
}

class LocalDateTimeSerializer implements JsonSerializer< LocalDateTime > {
	@Override
	public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
		return new JsonPrimitive(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime));
	}
}

class LocalDateTimeDeserializer implements JsonDeserializer < LocalDateTime > {
	@Override
	public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return LocalDateTime.parse(json.getAsString(),
				DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
}