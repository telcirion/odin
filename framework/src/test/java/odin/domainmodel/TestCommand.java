package odin.domainmodel;

import odin.common.Identity;
import odin.common.Version;

public class TestCommand extends Command {

    private static final long serialVersionUID = 1L;
    private final String testValue;

    public TestCommand(Identity id, Version targetVersion, String testValue) {
        super(id, targetVersion);
        this.testValue = testValue;
    }

    public String getTestValue() {
        return testValue;
    }

}