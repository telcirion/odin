package cqrs.concepts.common;

public interface IDispatcherBuilder{
	IDispatcher build();
	<T> IDispatcherBuilder dispatch(Class<T> msgClazz, IMessageAction<T> msgAction);
}