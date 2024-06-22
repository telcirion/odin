
package odin.domainmodel;

import java.time.LocalDateTime;
import java.util.UUID;

import odin.common.Message;
import odin.common.MessageInfo;

public abstract class DomainEvent implements Message {
    private MessageInfo messageInfo;
    private UUID aggregateRootId;

    public DomainEvent() {

    }

    public DomainEvent(final UUID aggregateId) {
        this.messageInfo = new MessageInfo(UUID.randomUUID(), LocalDateTime.now());
        this.aggregateRootId = aggregateId;
    }

    public LocalDateTime getTimestamp() {
        return messageInfo.timestamp();
    }

    public UUID getAggregateRootId() {
        return this.aggregateRootId;
    }

    public UUID getEventId() {
        return messageInfo.messageId();
    }

    public MessageInfo getMessageInfo() {
        return messageInfo;
    }

}
