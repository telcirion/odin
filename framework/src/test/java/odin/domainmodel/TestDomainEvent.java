
package odin.domainmodel;

import java.util.UUID;

public class TestDomainEvent extends DomainEvent {

    private final String eventData;

    public TestDomainEvent(final UUID aggregateId, String eventData) {
        super(aggregateId);
        this.eventData = eventData;
    }

    public String getEventData() {
        return eventData;
    }

}
