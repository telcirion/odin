
package odin.domainmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import odin.common.Identity;

class AggregateTest {

    private static Identity aggregateId;
    private static AggregateRoot aggregateRoot;
    private static Aggregate<AggregateRoot> sut;

    @BeforeAll
    static void setUp() {
        aggregateId = new Identity();
        aggregateRoot = new TestAggregateRoot();
        sut = new Aggregate<>(aggregateRoot);
    }

    @Test
    void getAggregateRoot() {
        assertEquals(aggregateRoot, sut.getAggregateRoot());
    }

    @Test
    void processAndgetAddedEvents() {
        sut.process(new TestCommand(aggregateId, null, "value 1"));
        sut.process(new TestCommand(aggregateId, null, "value 2"));

        assertEquals(2, sut.getAddedEvents().size());
    }
}
