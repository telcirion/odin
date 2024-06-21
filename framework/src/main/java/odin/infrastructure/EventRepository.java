
package odin.infrastructure;

import java.util.List;

import odin.applicationservices.CreateAggregateRoot;
import odin.applicationservices.Repository;
import odin.common.Identity;
import odin.common.SendMessage;
import odin.domainmodel.Aggregate;
import odin.domainmodel.AggregateRoot;
import odin.domainmodel.DomainEvent;

public class EventRepository<T extends AggregateRoot> implements Repository<T> {

    private final EventStore es;
    private final SendMessage eventBus;

    public EventRepository(final EventStore es, final SendMessage eventBus) {
        this.eventBus = eventBus;
        this.es = es;
    }

    @Override
    public Aggregate<T> load(Identity id, CreateAggregateRoot<T> creator) {
        var aggregateRoot = creator.createAggregateRoot();
        final List<DomainEvent> resultSet = es.load(id);
        resultSet.forEach(aggregateRoot::source);
        return new Aggregate<>(id, aggregateRoot);
    }

    @Override
    public void save(final Aggregate<T> obj) {
        obj.getAddedEvents().forEach(e -> {
            es.save(e);
            eventBus.send(e);
        });
    }
}
