
package odin.example.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import odin.domainmodel.DomainEvent;
import odin.infrastructure.EventStore;

@Component
public class SpringEventStore implements EventStore {

    final PersistableDomainEventRepo repo;

    public SpringEventStore(@Autowired PersistableDomainEventRepo repo) {
        super();
        this.repo = repo;
    }

    @Override
    public void save(DomainEvent domainEvent) {
        PersistableDomainEvent persistableDomainEvent = new PersistableDomainEvent(domainEvent);
        repo.save(persistableDomainEvent);
    }

    @Override
    public List<DomainEvent> load(UUID id) {
        List<PersistableDomainEvent> persistableDomainEvents = repo.findByAggregateIdOrderByTimestampAsc(id);
        List<DomainEvent> domainEvents = new ArrayList<>();
        persistableDomainEvents.forEach(e -> domainEvents.add(e.unwrap()));
        return domainEvents;
    }

    @Override
    public List<DomainEvent> load() {
        Iterable<PersistableDomainEvent> persistableDomainEvents = repo.findAll();
        List<DomainEvent> domainEvents = new ArrayList<>();
        persistableDomainEvents.forEach(e -> domainEvents.add(e.unwrap()));
        return domainEvents;
    }

}
