
package odin.infrastructure;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.common.Message;
import odin.common.MessageHandler;
import odin.common.MessageSender;

public class SimplePubSub implements MessageSender {
    final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final List<MessageHandler> subscribers = new ArrayList<>();

    @Override
    public void send(Message m) {
        subscribers.forEach(x -> x.handle(m));
    }

    @Override
    public void subscribe(MessageHandler messageHandler) {
        subscribers.add(messageHandler);
    }
}
