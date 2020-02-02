package odin.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AbstractDomainEventTest {
    private static ConcreteDomainEvent sut;
    private static UUID aggregateId;

    @BeforeAll
    static void setUp() {
        aggregateId = UUID.randomUUID();
        sut = new ConcreteDomainEvent(aggregateId);
    }

    private static class ConcreteDomainEvent extends AbstractDomainEvent {

        private static final long serialVersionUID = 1L;

        protected ConcreteDomainEvent(UUID aggregateId) {
            super(aggregateId);
        }
    }

    @Test
    void getEventId() {
        assertNotNull(sut.getEventId());
    }

    @Test
    void getAggregateId() {
        assertEquals(aggregateId, sut.getAggregateId());
    }

    @Test
    void getTimestamp() {
        assertNotNull(sut.getTimestamp());
    }
}