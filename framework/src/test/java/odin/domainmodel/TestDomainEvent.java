
package odin.domainmodel;

import odin.common.Identity;

public class TestDomainEvent extends DomainEvent {

    private final String eventData;

    public TestDomainEvent(final Identity aggregateId, String eventData) {
        super(aggregateId);
        this.eventData = eventData;
    }

    public String getEventData() {
        return eventData;
    }

}
