
package odin.domainmodel;

import java.util.ArrayList;
import java.util.List;

import odin.common.Identity;

public class Aggregate<T extends AggregateRoot> {

    private final List<DomainEvent> addedEvents;
    private final T aggregateRoot;

    public Aggregate(final Identity id, final T aggregateRoot) {
        this.aggregateRoot = aggregateRoot;
        this.addedEvents = new ArrayList<>();
    }

    public List<DomainEvent> getAddedEvents() {
        return addedEvents;
    }

    public T getAggregateRoot() {
        return aggregateRoot;
    }

    public DomainEvent process(Command command) {
        var event = aggregateRoot.process(command);
        this.addedEvents.add(event);
        return event;
    }
}
