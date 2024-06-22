
package odin.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import odin.common.Message;
import odin.common.MessageHandler;
import odin.common.MessageSender;
import odin.domainmodel.Aggregate;
import odin.domainmodel.TestAggregateRoot;
import odin.domainmodel.TestCommand;

class EventRepositoryTest {
    @Test
    void eventRepositoryTest() {
        InMemoryEventStore eventStore = new InMemoryEventStore();

        var sut = new EventRepository<TestAggregateRoot>(eventStore, new TestBus());

        var id = UUID.randomUUID();
        var saveAggregate = new Aggregate<>(new TestAggregateRoot());
        saveAggregate.process(new TestCommand(id, null, "value 1"));
        assertNotNull(saveAggregate.getAddedEvents().get(0).getTimestamp());
        sut.save(saveAggregate);
        var loadAggregate = sut.load(id, TestAggregateRoot::new);
        assertEquals(saveAggregate.getAggregateRoot().getTestField(), loadAggregate.getAggregateRoot().getTestField());
    }

    private class TestBus implements MessageSender {

        @Override
        public void send(Message m) {
        }

        @Override
        public void subscribe(MessageHandler messageHandler) {

        }

    }
}
