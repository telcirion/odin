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
