package odin.framework.infrastructure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import odin.concepts.common.IMessage;
import odin.concepts.common.IMessageHandler;
import odin.framework.common.TestMessage;

class SimpleMessageBusTest implements IMessageHandler {
    private boolean msgHandled = false;

    @Test
    void verySimpleMessageBusTest() {
        var sut = new SimpleMessageBus(SimpleMessageBus.BusType.QUEUE);
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
    public IMessageHandler handle(IMessage msg) {
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