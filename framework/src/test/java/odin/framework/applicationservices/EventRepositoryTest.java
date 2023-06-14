package odin.framework.applicationservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import odin.concepts.common.Identity;
import odin.concepts.common.Message;
import odin.concepts.common.SendMessage;
import odin.framework.domainmodel.EventAggregate;
import odin.framework.domainmodel.TestAggregateRoot;
import odin.framework.domainmodel.TestCommand;
import odin.framework.infrastructure.InMemoryEventStore;

class EventRepositoryTest {
    @Test
    void eventRepositoryTest() {
        InMemoryEventStore eventStore = new InMemoryEventStore();
        var sut = new EventRepository<TestAggregateRoot>(eventStore, new TestBus());

        var id = new Identity();
        var saveAggregate = new EventAggregate<>(id, new TestAggregateRoot());
        saveAggregate.process(new TestCommand(id, null, "value 1"));
        assertNotNull(saveAggregate.getAddedEvents().get(0).getMessageInfo().timestamp());
        sut.save(saveAggregate);
        var loadAggregate = sut.load(id, TestAggregateRoot::new);
        assertEquals(saveAggregate.getAggregateRoot().getTestField(), loadAggregate.getAggregateRoot().getTestField());
    }

    private class TestBus implements SendMessage {

        @Override
        public void send(Message m) {
        }

    }
}