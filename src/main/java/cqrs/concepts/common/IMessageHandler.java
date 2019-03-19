package cqrs.concepts.common;

public interface IMessageHandler {
	IDispatcher getDispatcher();
}
