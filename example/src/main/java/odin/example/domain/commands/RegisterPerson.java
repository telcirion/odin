
package odin.example.domain.commands;

import java.util.UUID;

import lombok.NoArgsConstructor;
import odin.domainmodel.Command;

@NoArgsConstructor
public class RegisterPerson extends Command {

    private String firstName;
    private String lastName;

    public RegisterPerson(UUID targetId, String lastName, String firstName) {
        super(targetId, null);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

}
