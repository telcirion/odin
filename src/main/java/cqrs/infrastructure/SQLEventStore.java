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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import cqrs.concepts.applicationservices.IRepository;
import cqrs.concepts.domainmodel.IAggregateRoot;
import cqrs.concepts.domainmodel.IDomainEvent;
import cqrs.concepts.infra.IDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLEventStore<T extends IAggregateRoot<T>> implements IRepository<T> {
	private final IDataSource ds;
	
	public SQLEventStore(IDataSource ds) {
		this.ds=ds;
	}
	
	private static String getSchemaDDL(){
		return "CREATE SCHEMA IF NOT EXISTS EVENTSTORE;\r\n"
		+ "DROP TABLE IF EXISTS EVENTSTORE.AGGREGATE;\r\n"
		+ "CREATE TABLE IF NOT EXISTS  EVENTSTORE.AGGREGATE(ID UUID PRIMARY KEY, CLASSNAME VARCHAR(255));\r\n"
		+ "DROP TABLE IF EXISTS EVENTSTORE.EVENT;\r\n"
		+ "CREATE TABLE IF NOT EXISTS  EVENTSTORE.EVENT(ID UUID PRIMARY KEY,  AGGREGATE_ID UUID NOT NULL , TIMESTAMP TIMESTAMP, CLASSNAME VARCHAR(255), DATA VARCHAR(MAX),"
		+ "FOREIGN KEY (AGGREGATE_ID) REFERENCES EVENTSTORE.AGGREGATE(ID));";
	}

	public static void createDatabase(IDataSource ds) {
		String sqlStmt = getSchemaDDL();
		try {
			Statement statement = ds.getConnection().createStatement();
			statement.executeUpdate(sqlStmt);
		} catch (SQLException ex) {
			Logger.getLogger(SQLEventStore.class.getName()).log(Level.SEVERE, ex.getMessage());
		}
	}

	@Override
	public void create(IAggregateRoot<T> obj) {
		String sqlStmt = "INSERT INTO EVENTSTORE.AGGREGATE (ID, CLASSNAME) VALUES ('"
				+ obj.getId() +"','" + obj.getClass().getName() + "');";
		sqlStmt=sqlStmt+eventInserts(obj);
		try {
			Statement statement = ds.getConnection().createStatement();
			statement.executeUpdate(sqlStmt);
		} catch (SQLException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage());
			throw new RuntimeException(ex);
		}
	}

	private String eventInserts(IAggregateRoot<T> obj) {
		StringBuilder b=new StringBuilder();
		obj.getEvents().forEach(s-> b.append("INSERT INTO EVENTSTORE.EVENT (ID, AGGREGATE_ID, TIMESTAMP, CLASSNAME, DATA) VALUES ('").append(s.getEventId()).append("','").append(obj.getId()).append("','").append(s.getTimestamp()).append("','").append(s.getClass().getName()).append("','").append(new Gson().toJson(s)).append("');\r\n"));
		return b.toString();
	}
	
	@Override
	public void update(IAggregateRoot<T> obj) {
		
		try {
			Statement statement = ds.getConnection().createStatement();
			statement.executeUpdate(eventInserts(obj));
		} catch (SQLException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage());
			throw new RuntimeException(ex);
		}
	}

	@Override
	public T get(IAggregateRoot<T> aggregate) {

		try {
			Statement statement = ds.getConnection().createStatement();
			ResultSet a=statement.executeQuery("SELECT CLASSNAME, DATA FROM EVENTSTORE.EVENT WHERE AGGREGATE_ID='"+aggregate.getId()+"' ORDER BY TIMESTAMP;");
			Gson g=new Gson();
			while (a.next()) {
				aggregate= aggregate.applyEvent((IDomainEvent) g.fromJson(a.getString(2), Class.forName(a.getString(1))));
			}
			return aggregate.getSnapshot();
		} catch (SQLException | JsonSyntaxException | ClassNotFoundException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage());
			throw  new RuntimeException(ex);
		}
	}
}