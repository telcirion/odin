
package odin.example.domain.events;

import lombok.NoArgsConstructor;
import odin.common.Identity;
import odin.domainmodel.DomainEvent;

@NoArgsConstructor
public class PersonRegistered extends DomainEvent {

    private String lastName;
    private String firstName;

    public PersonRegistered(Identity id, String lastName, String firstName) {
        super(id);
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

}
