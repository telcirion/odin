package cqrs.concepts.applicationservices;

import cqrs.concepts.common.IMessage;

import java.util.UUID;

public interface ICommand extends IMessage {
	UUID getTargetId();
	UUID getTargetVersion();
}
