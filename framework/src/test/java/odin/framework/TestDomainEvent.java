package odin.framework;

import odin.concepts.common.IMessageInfo;
import odin.concepts.common.Identity;
import odin.concepts.domainmodel.IDomainEvent;

public class TestDomainEvent implements IDomainEvent {

    private static final long serialVersionUID = 1L;
    private final MessageInfo domainEventInfo;
    private final String eventData;

    public TestDomainEvent(final Identity aggregateId, String eventData) {
        this.domainEventInfo = new MessageInfo(aggregateId, null);
        this.eventData = eventData;
    }

    public String getEventData() {
        return eventData;
    }

    @Override
    public IMessageInfo getMessageInfo() {
        return domainEventInfo;
    }

}