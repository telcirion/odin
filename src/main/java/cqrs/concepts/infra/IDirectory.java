package cqrs.concepts.infra;

import cqrs.concepts.common.IDispatcher;
import cqrs.concepts.common.IMessageHandler;

import java.util.List;

public interface IDirectory {

	<T extends IMessageHandler> void registerHandler(T messageHandler);

	<T> List<IDispatcher> getDispatchers(Class<T> t);

}
