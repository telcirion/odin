package odin.infrastructure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import odin.concepts.common.IMessage;
import odin.concepts.common.IMessageHandler;

class SimpleMessageBusTest {
    private boolean msgHandled = false;

    @Test
    public void verySimpleMessageBusTest() {
        var sut = new SimpleMessageBus("vm:testEndpoint");
        sut.consume(new MsgHandler());
        sut.send(new IMessage() {
            private static final long serialVersionUID = 1L;
        });
        sut.stop();
        assertTrue(msgHandled);
    }

    private class MsgHandler implements IMessageHandler {

        @Override
        public <T> IMessageHandler dispatch(T msg) {
            msgHandled = true;
            return null;
        }

    }
}