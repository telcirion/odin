package odin.framework.domainmodel;

import odin.concepts.common.IMessageInfo;
import odin.concepts.common.Identity;
import odin.concepts.common.Version;
import odin.concepts.domainmodel.ICommand;
import odin.framework.common.MessageInfo;

public class TestCommand implements ICommand {

    private static final long serialVersionUID = 1L;
    private final MessageInfo commandInfo;
    private final String testValue;

    public TestCommand(Identity id, Version targetVersion, String testValue) {
        this.commandInfo = new MessageInfo(id, targetVersion);
        this.testValue = testValue;
    }

    public String getTestValue() {
        return testValue;
    }

    @Override
    public IMessageInfo getMessageInfo() {
        return commandInfo;
    }

}