package odin.infrastructure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import odin.common.Message;
import odin.common.MessageHandler;
import odin.common.TestMessage;
import odin.infrastructure.SimplePubSub;

class SimplePubSubTest implements MessageHandler {
    private boolean msgHandled = false;

    @Test
    void verySimplePubSubTest() {
        var sut = new SimplePubSub();
        sut.subscribe(this);
        sut.send(new TestMessage());
        // noinspection StatementWithEmptyBody
        while (!isMsgHandled()) {
            // just wait;
        }
        sut.stop();
        assertTrue(msgHandled);
    }

    @Override
    public MessageHandler handle(Message msg) {
        synchronized (this) {
            msgHandled = true;
        }
        return null;
    }

    public boolean isMsgHandled() {
        synchronized (this) {
            return msgHandled;
        }
    }
}