
package odin.domainmodel;

import java.util.UUID;

public class TestCommand extends Command {

    private final String testValue;

    public TestCommand(UUID id, UUID targetVersion, String testValue) {
        super(id, targetVersion);
        this.testValue = testValue;
    }

    public String getTestValue() {
        return testValue;
    }

}
