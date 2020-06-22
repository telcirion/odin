package odin.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import odin.concepts.common.Identity;

class AbstractDomainEventTest {
    private static ConcreteDomainEvent sut;
    private static Identity aggregateId;

    @BeforeAll
    static void setUp() {
        aggregateId = new Identity();
        sut = new ConcreteDomainEvent(aggregateId);
    }

    private static class ConcreteDomainEvent extends AbstractDomainEvent {

        private static final long serialVersionUID = 1L;

        protected ConcreteDomainEvent(Identity aggregateId) {
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