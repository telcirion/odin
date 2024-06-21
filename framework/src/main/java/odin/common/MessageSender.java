
package odin.common;

public interface MessageSender {
    void send(Message m);

    void subscribe(MessageHandler messageHandler);

    default void stop() {
    }
}
