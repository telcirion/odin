package odin.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import odin.concepts.common.Identity;
import odin.concepts.domainmodel.IAggregate;
import odin.concepts.domainmodel.IAggregateRoot;

class AggregateTest {

    private static Identity aggregateId;
    private static IAggregateRoot aggregateRoot;
    private static IAggregate<IAggregateRoot> sut;

    @BeforeAll
    static void setUp() {
        aggregateId = new Identity();
        aggregateRoot = new TestAggregateRoot();
        sut = new Aggregate<>(aggregateId, aggregateRoot);
    }

    @Test
    void getAggregateRoot() {
        assertEquals(aggregateRoot, sut.getAggrageRoot());
    }

    @Test
    void processAndgetAddedEvents() {
        sut.process(new TestCommand(aggregateId, null, "value 1"));
        sut.process(new TestCommand(aggregateId, null, "value 2"));
        
        assertEquals(2, sut.getAddedEvents().size());
    }

    @Test
    void getId() {
        assertEquals(aggregateId, sut.getId());
    }

}