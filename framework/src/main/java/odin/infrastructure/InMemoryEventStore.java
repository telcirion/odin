
package odin.infrastructure;

import java.util.ArrayList;
import java.util.List;

import odin.common.Identity;
import odin.domainmodel.DomainEvent;

public class InMemoryEventStore implements EventStore {

    List<DomainEvent> eventStore = new ArrayList<>();

    @Override
    public void save(DomainEvent domainEvents) {
        eventStore.add(domainEvents);
    }

    @Override
    public List<DomainEvent> load(Identity id) {
        List<DomainEvent> filteredEvents = new ArrayList<>();

        filteredEvents.addAll(
                eventStore.stream().filter(c -> c.getMessageInfo().objectId().getId().equals(id.getId())).toList());
        return filteredEvents;
    }

}
