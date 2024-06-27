
package odin.example.domain.commands;

import java.util.UUID;

import lombok.NoArgsConstructor;
import odin.domainmodel.Command;

@NoArgsConstructor
public class ChangePersonName extends Command {

    private String firstName;

    public ChangePersonName(String name, UUID targetId, UUID targetVersion) {
        super(targetId, targetVersion);
        this.firstName = name;
    }

    public String getFirstName() {
        return this.firstName;
    }

}
