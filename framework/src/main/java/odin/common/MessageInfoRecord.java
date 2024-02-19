
package odin.common;

import java.time.LocalDateTime;

public record MessageInfoRecord(Identity messageId, LocalDateTime timestamp, Identity subjectId, Version subjectVersion)
        implements MessageInfo {
}
