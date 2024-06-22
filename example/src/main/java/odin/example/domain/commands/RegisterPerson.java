
package odin.example.domain.commands;

import java.util.UUID;

import odin.domainmodel.Command;

public class RegisterPerson extends Command {

    private final String firstName;
    private final String lastName;

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
