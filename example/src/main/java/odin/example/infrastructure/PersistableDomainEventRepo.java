package odin.example.infrastructure;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import odin.common.Identity;

public interface PersistableDomainEventRepo extends CrudRepository<PersistableDomainEvent, Long> {
    List<PersistableDomainEvent> findByAggregateIdOrderByTimestampAsc(Identity aggregateId);

}
