package odin.framework;

import java.util.UUID;

import odin.concepts.common.Identity;
import odin.concepts.domainmodel.ICommand;
import odin.concepts.domainmodel.ICommandInfo;

public class TestCommand implements ICommand {

    private static final long serialVersionUID = 1L;
    private final CommandInfo commandInfo;
    private final String testValue;

    public TestCommand(Identity id, UUID targetVersion, String testValue) {
        this.commandInfo = new CommandInfo(id, targetVersion);
        this.testValue = testValue;
    }

    public String getTestValue() {
        return testValue;
    }

    @Override
    public ICommandInfo getCommandInfo() {
        return commandInfo;
    }

}