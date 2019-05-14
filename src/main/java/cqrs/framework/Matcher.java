package cqrs.framework;

import cqrs.concepts.common.IMessageAction;
import cqrs.concepts.common.IMessageHandler;


public class Matcher{

    private final Object msg;
    public <T> Matcher(T msg){
        this.msg=msg;
    }
    @SuppressWarnings( { "unchecked" })
    public <T> Matcher match(Class<T> msgClazz, IMessageAction<T> msgAction) {
        if(msg.getClass().equals(msgClazz)){
            msgAction.executeAction((T)msg);
        }
        return this;
    }

    public IMessageHandler end(){
        return null;
    }
}