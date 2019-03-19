package cqrs.framework;

import cqrs.concepts.common.IDispatcher;
import cqrs.concepts.common.IMessageAction;

import java.util.Map;

class Dispatcher implements IDispatcher {
	private final  Map<Class<?>, IMessageAction<?>> msgActions;
	Dispatcher(Map<Class<?>, IMessageAction<?>> msgActions){
		this.msgActions=msgActions;
	}

	@Override
	@SuppressWarnings( { "unchecked" } )
	public <T,M> T dispatch(M msg) {
		return (T)((IMessageAction<M>) msgActions.get(msg.getClass())).executeAction(msg);
	}

	@Override
	public Map<Class<?>, IMessageAction<?>> getSupportedMessageTypes() {
		return this.msgActions;
	}
}