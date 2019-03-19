package cqrs.framework;

import cqrs.concepts.applicationservices.ISendMessage;
import cqrs.concepts.common.IMessage;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class SimpleMessageBus<T extends IMessage> implements ISendMessage<T> {

	private final String endpoint;
	private final CamelContext ctx;

	public SimpleMessageBus(String endpoint){
		ctx=new DefaultCamelContext();
		this.endpoint=endpoint;
	}

	@Override
	public void send(T m) {
		ctx.createProducerTemplate().sendBody(endpoint, m);
	}
}
