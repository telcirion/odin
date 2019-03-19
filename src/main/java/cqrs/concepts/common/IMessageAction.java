package cqrs.concepts.common;

@FunctionalInterface
public interface IMessageAction<T> {
	IMessageHandler executeAction(T msg);
}