package odin.framework;

import odin.concepts.common.Identity;
import odin.concepts.domainmodel.IDomainEvent;
import odin.concepts.domainmodel.IDomainEventInfo;

public class TestDomainEvent implements IDomainEvent {

    private static final long serialVersionUID = 1L;
    private final DomainEventInfo domainEventInfo;
    private final String eventData;

    public TestDomainEvent(final Identity aggregateId, String eventData) {
        this.domainEventInfo = new DomainEventInfo(aggregateId);
        this.eventData = eventData;
    }

    public String getEventData() {
        return eventData;
    }

    @Override
    public IDomainEventInfo getDomainEventInfo() {
        return domainEventInfo;
    }

}