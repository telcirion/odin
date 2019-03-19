package cqrs.framework;

import cqrs.concepts.common.IDispatcher;
import cqrs.concepts.common.IDispatcherBuilder;
import cqrs.concepts.common.IMessageAction;

import java.util.HashMap;
import java.util.Map;

public class DispatcherBuilder implements IDispatcherBuilder {
	
	private final Map<Class<?>, IMessageAction<?>> msgActions = new HashMap<>();
	
	@Override
	public <T> IDispatcherBuilder dispatch(Class<T> msgClazz, IMessageAction<T> msgAction) {
		msgActions.put(msgClazz, msgAction);
		return this;
	}

	@Override
	public IDispatcher build() {
		return new Dispatcher(msgActions);
	}
}