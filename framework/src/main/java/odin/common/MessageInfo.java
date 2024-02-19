
package odin.common;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface MessageInfo extends Serializable {
    Identity messageId();

    Identity subjectId();

    Version subjectVersion();

    LocalDateTime timestamp();
}
