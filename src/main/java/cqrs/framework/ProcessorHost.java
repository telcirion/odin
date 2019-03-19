package cqrs.framework;

import cqrs.concepts.infra.IDirectory;
import cqrs.concepts.infra.IProcessorHost;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ProcessorHost implements IProcessorHost {
	private int called = 0;

	private final IDirectory directory;


	public ProcessorHost(String inEndpoint, IDirectory handlers) {

		CamelContext ctx = new DefaultCamelContext();
		this.directory=handlers;
		RouteBuilder builder = new RouteBuilder() {
			public void configure() {
				errorHandler(deadLetterChannel("mock:error"));
				from(inEndpoint).bean(ProcessorHost.this, "process(*)");
			}
		};
		try {
			ctx.addRoutes(builder);
			ctx.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void process(Object m) {
		directory.getDispatchers(m.getClass()).forEach(s -> s.dispatch(m));
		synchronized (this) {
			called++;
		}
	}

	public int getCalled() {
		synchronized (this) {
			return called;
		}
	}
}
