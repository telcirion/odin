package odin.framework.domainmodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import odin.concepts.common.IMessageInfo;
import odin.concepts.common.Identity;
import odin.concepts.domainmodel.IDomainEvent;
import odin.framework.common.MessageInfo;

public class TestDomainEvent implements IDomainEvent {

    private static final long serialVersionUID = 1L;
    private final MessageInfo messageInfo;
    private final String eventData;

    public TestDomainEvent(final Identity aggregateId, String eventData) {
        this.messageInfo = new MessageInfo(aggregateId, null);
        this.eventData = eventData;
    }

    public String getEventData() {
        return eventData;
    }

    @Override
    public IMessageInfo getMessageInfo() {
        return messageInfo;
    }

}