
package odin.domainmodel;

import java.time.LocalDateTime;

import odin.common.Identity;
import odin.common.Message;
import odin.common.MessageInfo;
import odin.common.MessageInfoRecord;
import odin.common.Version;

public abstract class Command implements Message {
    private MessageInfoRecord messageInfo;

    public Command(Identity id, Version targetVersion) {
        this.messageInfo = new MessageInfoRecord(new Identity(), LocalDateTime.now(), id, targetVersion);
    }

    @Override
    public MessageInfo getMessageInfo() {
        return messageInfo;
    }
}
