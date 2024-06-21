
package odin.example.domain.events;

import lombok.NoArgsConstructor;
import odin.common.Identity;
import odin.domainmodel.DomainEvent;

@NoArgsConstructor
public class PersonNameChanged extends DomainEvent {

    private String firstName;

    public PersonNameChanged(Identity aggregateId, String firstName) {
        super(aggregateId);
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

}
