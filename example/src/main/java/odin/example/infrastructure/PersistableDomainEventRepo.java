
package odin.example.infrastructure;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface PersistableDomainEventRepo extends CrudRepository<PersistableDomainEvent, Long> {
    List<PersistableDomainEvent> findByAggregateIdOrderByTimestampAsc(UUID aggregateId);

}
