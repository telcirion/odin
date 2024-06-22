
package odin.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import odin.domainmodel.TestDomainEvent;

class InMemoryEventStoreTest {
    @Test
    void eventStoreTest() {
        var eventStore = new InMemoryEventStore();

        UUID aggregateId1 = UUID.randomUUID();
        UUID aggregateId2 = UUID.randomUUID();

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

        assertEquals(testDomainEvent1.getEventId().toString(),
                loadedEvents1.getFirst().getEventId().toString());
    }
}
