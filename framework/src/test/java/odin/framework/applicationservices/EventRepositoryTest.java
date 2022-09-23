package odin.framework.applicationservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import odin.concepts.common.Identity;
import odin.concepts.common.Message;
import odin.concepts.common.SendMessage;
import odin.concepts.infra.DataSource;
import odin.framework.domainmodel.EventAggregate;
import odin.framework.domainmodel.TestAggregateRoot;
import odin.framework.domainmodel.TestCommand;
import odin.framework.infrastructure.SqlEventStore;

class EventRepositoryTest {
    @Test
    void eventRepositoryTest() {
        var eventStore = new SqlEventStore(new TestDataSource());
        eventStore.createDatabase();
        var sut = new EventRepository<TestAggregateRoot>(eventStore, new TestBus());

        var id = new Identity();
        var saveAggregate = new EventAggregate<>(id, new TestAggregateRoot());
        saveAggregate.process(new TestCommand(id, null, "value 1"));
        assertNotNull(saveAggregate.getAddedEvents().get(0).getMessageInfo().timestamp());
        sut.save(saveAggregate);
        var loadAggregate = sut.load(id, TestAggregateRoot::new);
        assertEquals(saveAggregate.getAggregateRoot().getTestField(), loadAggregate.getAggregateRoot().getTestField());
    }

    private static class TestDataSource implements DataSource {

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

    private class TestBus implements SendMessage {

        @Override
        public void send(Message m) {
        }

    }
}