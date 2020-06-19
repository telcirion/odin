package odin.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.junit.jupiter.api.Test;

import odin.concepts.common.IMessage;
import odin.concepts.common.IMessageHandler;
import odin.concepts.common.ISendMessage;
import odin.concepts.infra.IDataSource;
import odin.framework.AbstractAggregateRoot;
import odin.framework.AbstractDomainEvent;
import odin.framework.Matcher;

class SqlEventRepositoryTest {
    @Test
    public void sqlEventRepositoryTest() {

        var sut = new SqlEventRepository<TestAggregate>(new TestDataSource(), new TestBus());
        sut.createDatabase();
        var id = UUID.randomUUID();
        var saveAggregate = new TestAggregate(id);
        saveAggregate.applyEvent(new Msg(id, "test"));
        sut.save(saveAggregate);
        TestAggregate loadAggregate = sut.load(new TestAggregate(id));
        assertEquals(saveAggregate.testField, loadAggregate.testField);
    }

    private class Msg extends AbstractDomainEvent {
        private static final long serialVersionUID = 1L;
        public final String testField;

        protected Msg(UUID aggregateId, String testField) {
            super(aggregateId);
            this.testField = testField;
        }
    }

    private class TestAggregate extends AbstractAggregateRoot {

        public String testField;

        protected TestAggregate(UUID id) {
            super(id);
        }

        private TestAggregate setTestField(Msg msg) {
            this.testField = msg.testField;
            return this;
        }

        @Override
        public <T> IMessageHandler handle(T msg) {
            return new Matcher(this).match(Msg.class, this::setTestField, msg).result();
        }

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