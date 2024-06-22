
package odin.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record MessageInfo(UUID messageId, LocalDateTime timestamp)
        implements Serializable {
}
