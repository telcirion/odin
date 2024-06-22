
package odin.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import odin.domainmodel.DomainEvent;

public class InMemoryEventStore implements EventStore {

    List<DomainEvent> eventStore = new ArrayList<>();

    @Override
    public void save(DomainEvent domainEvents) {
        eventStore.add(domainEvents);
    }

    @Override
    public List<DomainEvent> load(UUID id) {
        List<DomainEvent> filteredEvents = new ArrayList<>();

        filteredEvents.addAll(
                eventStore.stream().filter(c -> c.getAggregateRootId().equals(id)).toList());
        return filteredEvents;
    }

}
