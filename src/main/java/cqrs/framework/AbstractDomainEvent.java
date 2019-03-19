package cqrs.framework;

import cqrs.concepts.domainmodel.IDomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AbstractDomainEvent implements IDomainEvent {

	private final UUID id=UUID.randomUUID();
	private final UUID aggregateId;
	private final LocalDateTime timestamp;

	protected AbstractDomainEvent(){
		this.aggregateId=null;
		this.timestamp=LocalDateTime.now();
	}

	protected AbstractDomainEvent(UUID aggregateId) {
		this.aggregateId=aggregateId;
		this.timestamp=LocalDateTime.now();
	}

	@Override
	public UUID getEventId() {
		return id;
	}

	@Override
	public UUID getAggregateId() {
		return aggregateId;
	}

	@Override
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
}
