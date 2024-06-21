
package odin.example.domain.commands;

import odin.common.Identity;
import odin.common.Version;
import odin.domainmodel.Command;

public class ChangePersonName extends Command {

    private final String firstName;

    public ChangePersonName(String name, Identity targetId, Version targetVersion) {
        super(targetId, targetVersion);
        this.firstName = name;
    }

    public String getFirstName() {
        return this.firstName;
    }

}
