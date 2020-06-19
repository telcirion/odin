package odin.infrastructure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import odin.concepts.common.IMessage;
import odin.concepts.common.IMessageHandler;

class SimpleMessageBusTest implements IMessageHandler, IMessage {
    private static final long serialVersionUID = 1L;
    private boolean msgHandled = false;

    @Test
    public void verySimpleMessageBusTest() {
        var sut = new SimpleMessageBus(SimpleMessageBus.BusType.QUEUE);
        sut.consume(this);
        sut.send(this);
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