package cqrs.concepts.applicationservices;

import cqrs.concepts.common.IMessage;

public interface IMessageBusFactory {
    <T extends IMessage> ISendMessage<T> getMessageBus(String outboundEndpoint);
}
