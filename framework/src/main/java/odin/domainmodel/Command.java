
package odin.domainmodel;

import java.time.LocalDateTime;

import odin.common.Identity;
import odin.common.Message;
import odin.common.MessageInfoRecord;
import odin.common.Version;

public abstract class Command implements Message {
    private MessageInfoRecord messageInfo;

    private final Identity aggregateRootId;
    private final Version aggregateVersion;

    public Command(Identity id, Version targetVersion) {
        this.messageInfo = new MessageInfoRecord(new Identity(), LocalDateTime.now(), null, null);
        this.aggregateRootId = id;
        this.aggregateVersion = targetVersion;
    }

    public Identity getAggregateRootId() {
        return aggregateRootId;
    }

    public Version getAggregateVersion() {
        return aggregateVersion;
    }
}
