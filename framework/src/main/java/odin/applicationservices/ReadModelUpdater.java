
package odin.applicationservices;

import odin.common.MessageHandler;

public interface ReadModelUpdater<T> extends MessageHandler {
    T getReadModelRepository();
}
