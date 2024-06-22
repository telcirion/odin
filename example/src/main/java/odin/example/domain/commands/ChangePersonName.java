
package odin.example.domain.commands;

import java.util.UUID;

import odin.domainmodel.Command;

public class ChangePersonName extends Command {

    private final String firstName;

    public ChangePersonName(String name, UUID targetId, UUID targetVersion) {
        super(targetId, targetVersion);
        this.firstName = name;
    }

    public String getFirstName() {
        return this.firstName;
    }

}
