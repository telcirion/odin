package odin.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import odin.concepts.common.Identity;
import odin.concepts.domainmodel.IDomainEventInfo;

class DomainEventInfoTest {
    private static IDomainEventInfo sut;
    private static Identity aggregateId;

    @BeforeAll
    static void setUp() {
        aggregateId = new Identity();
        sut = new DomainEventInfo(aggregateId);
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