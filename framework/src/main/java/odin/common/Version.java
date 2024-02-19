
package odin.common;

import java.io.Serializable;
import java.util.UUID;

public class Version implements Serializable {

    private static final long serialVersionUID = 1L;
    final UUID versionNumber;

    public Version() {
        this.versionNumber = UUID.randomUUID();
    }
}
