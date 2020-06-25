package odin.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.junit.jupiter.api.Test;

import odin.concepts.common.IMessage;
import odin.concepts.common.ISendMessage;
import odin.concepts.common.Identity;
import odin.concepts.infra.IDataSource;
import odin.framework.Aggregate;
import odin.framework.TestAggregateRoot;
import odin.framework.TestCommand;

class SqlEventRepositoryTest {
    @Test
    void sqlEventRepositoryTest() {

        var sut = new SqlEventRepository(new TestDataSource(), new TestBus());
        sut.createDatabase();
        var id = new Identity();
        var saveAggregate = new Aggregate<>(id, new TestAggregateRoot());
        saveAggregate.process(new TestCommand(id, null, "value 1"));
        sut.save(saveAggregate);
        var loadAggregate = sut.load(new Aggregate<>(id, new TestAggregateRoot()));
        assertEquals(saveAggregate.getAggrateRoot().getTestField(), loadAggregate.getAggrateRoot().getTestField());
    }

    private static class TestDataSource implements IDataSource {

        private static final HikariConfig config = new HikariConfig();
        private static final HikariDataSource ds;

        static {
            config.setJdbcUrl("jdbc:h2:mem:test");
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
    }

    private class TestBus implements ISendMessage {

        @Override
        public void send(IMessage m) {
        }

    }
}