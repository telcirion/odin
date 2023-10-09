package odin.domainmodel;

import java.time.LocalDateTime;

import odin.common.Identity;
import odin.common.MessageInfo;
import odin.common.MessageInfoRecord;
import odin.common.Version;

public class TestCommand implements Command {

    private static final long serialVersionUID = 1L;
    private final MessageInfoRecord commandInfo;
    private final String testValue;

    public TestCommand(Identity id, Version targetVersion, String testValue) {
        this.commandInfo = new MessageInfoRecord(new Identity(), LocalDateTime.now(), id, targetVersion);
        this.testValue = testValue;
    }

    public String getTestValue() {
        return testValue;
    }

    @Override
    public MessageInfo getMessageInfo() {
        return commandInfo;
    }

}