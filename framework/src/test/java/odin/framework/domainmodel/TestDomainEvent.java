package odin.framework.domainmodel;

import java.time.LocalDateTime;

import odin.concepts.common.Identity;
import odin.concepts.common.MessageInfo;
import odin.concepts.domainmodel.DomainEvent;
import odin.framework.common.MessageInfoRecord;

public class TestDomainEvent implements DomainEvent {

    private static final long serialVersionUID = 1L;
    private final MessageInfoRecord messageInfo;
    private final String eventData;

    public TestDomainEvent(final Identity aggregateId, String eventData) {
        this.messageInfo = new MessageInfoRecord(new Identity(), LocalDateTime.now(), aggregateId, null);
        this.eventData = eventData;
    }

    public String getEventData() {
        return eventData;
    }

    @Override
    public MessageInfo getMessageInfo() {
        return messageInfo;
    }

}