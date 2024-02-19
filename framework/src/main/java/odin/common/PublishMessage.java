
package odin.common;

public interface PublishMessage {
    void subscribe(MessageHandler messageHandler);

    default void stop() {
    }
}
