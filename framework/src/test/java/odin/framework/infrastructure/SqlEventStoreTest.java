package odin.framework.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import odin.concepts.common.Identity;
import odin.concepts.infra.DataSource;
import odin.framework.domainmodel.TestDomainEvent;

class SqlEventStoreTest {
    @Test
    void eventStoreTest() {
        var eventStore = new SqlEventStore(new TestDataSource());
        eventStore.createDatabase();

        Identity aggregateId1 = new Identity();
        Identity aggregateId2 = new Identity();

        var testDomainEvent1 = new TestDomainEvent(aggregateId1, "event 1");
        var testDomainEvent2 = new TestDomainEvent(aggregateId2, "event 2");
        var testDomainEvent3 = new TestDomainEvent(aggregateId1, "event 3");

        eventStore.save(testDomainEvent1);
        eventStore.save(testDomainEvent2);
        eventStore.save(testDomainEvent3);

        var loadedEvents1 = eventStore.load(aggregateId1);
        var loadedEvents2 = eventStore.load(aggregateId2);

        assertEquals(2, loadedEvents1.size());
        assertEquals(1, loadedEvents2.size());

        assertEquals(testDomainEvent1.getMessageInfo().messageId().getId().toString(),
                loadedEvents1.get(0).getMessageInfo().messageId().getId().toString());
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
}