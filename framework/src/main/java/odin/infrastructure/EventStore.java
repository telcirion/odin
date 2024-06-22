
package odin.infrastructure;

import java.util.List;
import java.util.UUID;

import odin.domainmodel.DomainEvent;

public interface EventStore {
    void save(DomainEvent domainEvents);

    List<DomainEvent> load(UUID id);
}
