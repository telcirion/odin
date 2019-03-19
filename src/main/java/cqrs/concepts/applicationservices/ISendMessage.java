package cqrs.concepts.applicationservices;

import cqrs.concepts.common.IMessage;

public interface ISendMessage<T extends IMessage> {
    void send(T m);
}
