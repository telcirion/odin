package cqrs.framework;

import cqrs.concepts.common.IDispatcher;
import cqrs.concepts.common.IMessageAction;
import cqrs.concepts.common.IMessageHandler;
import cqrs.concepts.infra.IDirectory;

import java.util.ArrayList;
import java.util.List;

public class Directory implements IDirectory {
	
	class DispatcherEntry {
		final Object msg;
		final IDispatcher dispatcher;
		DispatcherEntry(Object msg, IDispatcher dispatcher) {
			super();
			this.msg = msg;
			this.dispatcher = dispatcher;
		}
	}
	
	private final List<DispatcherEntry> directory=new ArrayList<>();
	
	@Override
	public <T> List<IDispatcher> getDispatchers(Class<T> t) {
		List<IDispatcher> l =new ArrayList<>();
		directory.stream().filter(s -> s.msg.equals(t)).forEach(a -> l.add(a.dispatcher));
		return l;
	}

	@Override
	public <T extends IMessageHandler> void registerHandler(T messageHandler) {
		var a=messageHandler.getDispatcher();
		var b=a.getSupportedMessageTypes();
		b.forEach((Class<?> s, IMessageAction<?> m) -> directory.add(new DispatcherEntry(s,a)));
	}
}
