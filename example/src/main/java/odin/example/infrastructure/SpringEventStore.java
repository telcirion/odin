
package odin.example.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import odin.common.Identity;
import odin.domainmodel.DomainEvent;
import odin.infrastructure.EventStore;

@Component
public class SpringEventStore implements EventStore {
    @Autowired
    PersistableDomainEventRepo repo;

    @Override
    public void save(DomainEvent domainEvent) {
        PersistableDomainEvent persistableDomainEvent = new PersistableDomainEvent(domainEvent);
        repo.save(persistableDomainEvent);
    }

    @Override
    public List<DomainEvent> load(Identity id) {
        List<PersistableDomainEvent> persistableDomainEvents = repo.findByAggregateIdOrderByTimestampAsc(id);
        List<DomainEvent> domainEvents = new ArrayList<>();
        persistableDomainEvents.forEach(e -> domainEvents.add(e.unwrap()));
        return domainEvents;
    }

}
