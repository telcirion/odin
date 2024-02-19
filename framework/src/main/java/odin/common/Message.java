
package odin.common;

import java.io.Serializable;

public interface Message extends Serializable {
    MessageInfo getMessageInfo();
}
