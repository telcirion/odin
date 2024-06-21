
package odin.domainmodel;

import java.time.LocalDateTime;

import odin.common.Identity;
import odin.common.Message;
import odin.common.MessageInfoRecord;

public abstract class DomainEvent implements Message {
    private MessageInfoRecord messageInfo;

    public DomainEvent() {

    }

    public DomainEvent(final Identity aggregateId) {
        this.messageInfo = new MessageInfoRecord(new Identity(), LocalDateTime.now(), aggregateId, null);
    }

    public LocalDateTime getTimestamp() {
        return messageInfo.timestamp();
    }
  
    public Identity getAggregateRootId() {
        return messageInfo.objectId();
    }

    public Identity getEventId() {
        return messageInfo.messageId();
    }

}
