
package odin.domainmodel;

import java.time.LocalDateTime;

import odin.common.Identity;
import odin.common.Message;
import odin.common.MessageInfo;
import odin.common.MessageInfoRecord;

public abstract class DomainEvent implements Message {
    private MessageInfoRecord messageInfo;

    public DomainEvent() {

    }

    public DomainEvent(final Identity aggregateId) {
        this.messageInfo = new MessageInfoRecord(new Identity(), LocalDateTime.now(), aggregateId, null);
    }

    @Override
    public MessageInfo getMessageInfo() {
        return messageInfo;
    }
}
