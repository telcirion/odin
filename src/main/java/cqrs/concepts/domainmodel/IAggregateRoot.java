package cqrs.concepts.domainmodel;

import cqrs.concepts.common.IMessageHandler;

import java.util.List;
import java.util.UUID;

public interface IAggregateRoot<T extends IAggregateRoot<T>> extends IMessageHandler {
	UUID getId();
	List<IDomainEvent> getEvents();
	IAggregateRoot<T> applyEvent(IDomainEvent event);
	T getSnapshot();
}