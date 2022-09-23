package odin.framework.infrastructure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import odin.concepts.common.Message;
import odin.concepts.common.MessageHandler;
import odin.framework.common.TestMessage;

class SimplePubSubTest implements MessageHandler {
    private boolean msgHandled = false;

    @Test
    void verySimplePubSubTest() {
        var sut = new SimplePubSub();
        sut.subscribe(this);
        sut.send(new TestMessage());
        // noinspection StatementWithEmptyBody
        while (!isMsgHandled()){
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