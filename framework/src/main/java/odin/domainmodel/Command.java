
package odin.domainmodel;

import java.time.LocalDateTime;
import java.util.UUID;

import odin.common.Message;
import odin.common.MessageInfo;

public abstract class Command implements Message {
    private MessageInfo messageInfo;

    private UUID aggregateRootId;
    private UUID aggregateVersion;

    protected Command() {

    }

    protected Command(UUID id, UUID targetVersion) {
        this.messageInfo = new MessageInfo(UUID.randomUUID(), LocalDateTime.now());
        this.aggregateRootId = id;
        this.aggregateVersion = targetVersion;
    }

    public UUID getAggregateRootId() {
        return aggregateRootId;
    }

    public UUID getAggregateVersion() {
        return aggregateVersion;
    }

    public MessageInfo getMessageInfo() {
        return messageInfo;
    }
}
