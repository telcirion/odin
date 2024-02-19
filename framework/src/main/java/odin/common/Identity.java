
package odin.common;

import java.io.Serializable;
import java.util.UUID;

public class Identity implements Serializable {

    private static final long serialVersionUID = 1L;
    final UUID id;

    public Identity() {
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public UUID getId() {
        return id;
    }
}
