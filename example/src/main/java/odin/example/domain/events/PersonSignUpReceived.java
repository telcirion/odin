
package odin.example.domain.events;

import lombok.NoArgsConstructor;
import odin.common.Identity;
import odin.domainmodel.DomainEvent;

@NoArgsConstructor
public class PersonSignUpReceived extends DomainEvent {

    private String lastName;
    private String firstName;

    public PersonSignUpReceived(String lastName, String firstName) {
        super(new Identity());
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
