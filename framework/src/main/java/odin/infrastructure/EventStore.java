
package odin.infrastructure;

import java.util.List;

import odin.common.Identity;
import odin.domainmodel.DomainEvent;

public interface EventStore {
    void save(DomainEvent domainEvents);

    List<DomainEvent> load(Identity id);
}
