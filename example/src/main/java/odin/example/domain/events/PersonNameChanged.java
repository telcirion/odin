
package odin.example.domain.events;

import java.util.UUID;

import lombok.NoArgsConstructor;
import odin.domainmodel.DomainEvent;

@NoArgsConstructor
public class PersonNameChanged extends DomainEvent {

    private String firstName;

    public PersonNameChanged(UUID aggregateId, String firstName) {
        super(aggregateId);
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

}
