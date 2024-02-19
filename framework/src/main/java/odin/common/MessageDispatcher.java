
package odin.common;

public class MessageDispatcher<Z> {
    final Z defaultHandler;

    public MessageDispatcher(Z defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public <T> MessageDispatcher<Z> match(final Class<T> msgClazz, final MessageAction<T, Z> msgAction,
            final Message msg) {
        if (msgClazz.isInstance(msg)) {
            return new MessageDispatcher<>(msgAction.executeAction(msgClazz.cast(msg)));
        }
        return this;
    }

    public Z result() {
        return defaultHandler;
    }
}
