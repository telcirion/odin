package odin.domainmodel;

import odin.common.Identity;

public class TestDomainEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private final String eventData;

    public TestDomainEvent(final Identity aggregateId, String eventData) {
        super(aggregateId);
        this.eventData = eventData;
    }

    public String getEventData() {
        return eventData;
    }

}