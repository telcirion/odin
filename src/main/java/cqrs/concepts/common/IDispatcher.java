package cqrs.concepts.common;

import java.util.Map;

public interface IDispatcher{
	Map<Class<?>, IMessageAction<?>> getSupportedMessageTypes();
	<T,M> T dispatch(M msg);
}