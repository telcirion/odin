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

package cqrs.test.infra;

import java.sql.Connection;
import java.sql.SQLException;

import cqrs.concepts.infra.IDataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPDataSource implements IDataSource {

	private static final HikariConfig config = new HikariConfig();
	private static final HikariDataSource ds;

	static {
		//config.setJdbcUrl("jdbc:h2:mem:test");
		config.setJdbcUrl("jdbc:h2:tcp://localhost/~/cqrs-sample-test");
		config.setUsername("sa");
		config.setPassword("");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	public HikariCPDataSource() {
	}
}