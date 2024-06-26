
package odin.infrastructure;

import java.util.List;
import java.util.UUID;

import odin.applicationservices.CreateAggregateRoot;
import odin.applicationservices.Repository;
import odin.common.MessageSender;
import odin.domainmodel.Aggregate;
import odin.domainmodel.AggregateRoot;
import odin.domainmodel.DomainEvent;

public class EventRepository<T extends AggregateRoot> implements Repository<T> {

    private final EventStore es;
    private final MessageSender eventBus;

    public EventRepository(final EventStore es, final MessageSender eventBus) {
        this.eventBus = eventBus;
        this.es = es;
    }

    @Override
    public Aggregate<T> load(UUID id, CreateAggregateRoot<T> creator) {
        var aggregateRoot = creator.createAggregateRoot();
        final List<DomainEvent> resultSet = es.load(id);
        if (!resultSet.isEmpty()) {
            resultSet.forEach(aggregateRoot::source);
            return new Aggregate<>(aggregateRoot);
        } else {
            return null;
        }
    }

    @Override
    public void save(final Aggregate<T> obj) {
        obj.getAddedEvents().forEach(e -> {
            es.save(e);
            eventBus.send(e);
        });
    }
}
