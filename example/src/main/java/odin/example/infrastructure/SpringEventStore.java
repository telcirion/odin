package odin.example.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import odin.concepts.applicationservices.EventStore;
import odin.concepts.common.Identity;
import odin.concepts.domainmodel.DomainEvent;

@Component
public class SpringEventStore implements EventStore {
    @Autowired
    PersistableDomainEventRepo repo;

    @Override
    public void save(DomainEvent domainEvent) {
        PersistableDomainEvent wEvent = new PersistableDomainEvent(domainEvent);
        repo.save(wEvent);
    }

    @Override
    public List<DomainEvent> load(Identity id) {
        List<PersistableDomainEvent> wEvents = repo.findByAggregateIdOrderByTimestampAsc(id);
        List<DomainEvent> dEvents = new ArrayList<>();
        wEvents.forEach(e -> dEvents.add(e.unwrap()));
        return dEvents;
    }

}
