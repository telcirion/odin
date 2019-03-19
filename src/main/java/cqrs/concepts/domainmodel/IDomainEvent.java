package cqrs.concepts.domainmodel;

import cqrs.concepts.common.IMessage;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IDomainEvent extends IMessage {
	UUID getEventId();
	UUID getAggregateId();
	LocalDateTime getTimestamp();
}
