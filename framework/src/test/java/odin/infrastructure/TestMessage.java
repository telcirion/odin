package odin.infrastructure;

import odin.concepts.common.IMessage;
import odin.concepts.common.IMessageInfo;

public class TestMessage implements IMessage {
    private static final long serialVersionUID = 1L;

    @Override
    public IMessageInfo getMessageInfo() {
        return null;
    }
      
}