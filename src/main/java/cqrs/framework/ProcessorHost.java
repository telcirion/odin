// Copyright 2019 Peter Jansen
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
	
	@SuppressWarnings("unused") // called using camel
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
