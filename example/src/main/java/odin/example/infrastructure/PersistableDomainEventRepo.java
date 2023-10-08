package odin.example.infrastructure;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import odin.concepts.common.Identity;

public interface PersistableDomainEventRepo extends CrudRepository<PersistableDomainEvent, Long> {
    List<PersistableDomainEvent> findByAggregateIdOrderByTimestampAsc(Identity aggregateId);

}
