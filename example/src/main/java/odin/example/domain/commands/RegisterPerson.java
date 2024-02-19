
package odin.example.domain.commands;

import odin.common.Identity;
import odin.domainmodel.Command;

public class RegisterPerson extends Command {

    private static final long serialVersionUID = 1L;
    private final String firstName;
    private final String lastName;

    public RegisterPerson(Identity targetId, String lastName, String firstName) {
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
