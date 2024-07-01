
package odin.infrastructure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import odin.common.Message;
import odin.common.MessageHandler;
import odin.common.Result;
import odin.common.TestMessage;

class SimplePubSubTest implements MessageHandler {
    private boolean msgHandled = false;

    @Test
    void verySimplePubSubTest() {
        var sut = new SimplePubSub();
        sut.subscribe(this);
        sut.send(new TestMessage());
        // noinspection StatementWithEmptyBody
        while (!isMsgHandled()) {
            // do nothin, just wait
        }
        sut.stop();
        assertTrue(msgHandled);
    }

    @Override
    public Result handle(Message msg) {
        synchronized (this) {
            msgHandled = true;
        }
        return Result.OK;
    }

    public boolean isMsgHandled() {
        synchronized (this) {
            return msgHandled;
        }
    }
}
