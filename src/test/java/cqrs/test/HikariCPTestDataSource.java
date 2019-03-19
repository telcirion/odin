package cqrs.test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import cqrs.concepts.infra.IDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPTestDataSource implements IDataSource {

	private static final HikariConfig config = new HikariConfig();
	private static final HikariDataSource ds;

	static {
		config.setJdbcUrl("jdbc:h2:mem:test");
		//config.setJdbcUrl("jdbc:h2:tcp://localhost/~/cqrs-sample-test");
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

	public HikariCPTestDataSource() {
	}
}