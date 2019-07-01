/* Copyright 2019 Peter Jansen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package odin.infrastructure;

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;

import java.lang.invoke.MethodHandles;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import odin.concepts.applicationservices.IConsumeMessage;
import odin.concepts.applicationservices.ISendMessage;
import odin.concepts.common.IMessage;
import odin.concepts.common.IMessageHandler;

public class SimpleMessageBus implements ISendMessage, IConsumeMessage {
    final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String endpoint;
    private final CamelContext ctx;

    public SimpleMessageBus(String endpoint) {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");
        ctx = new DefaultCamelContext();
        ctx.setStreamCaching(true);
        ctx.addComponent("activemq", activeMQComponent("vm://localhost?broker.persistent=false"));
        this.endpoint = endpoint;
    }

    @Override
    public void send(IMessage m) {
        ctx.createProducerTemplate().sendBody(endpoint, m);
    }

    @Override
    public void consume(IMessageHandler messageHandler) {
        RouteBuilder builder = new RouteBuilder() {
            public void configure() {
                errorHandler(deadLetterChannel("mock:error"));
                from(endpoint).process().body(messageHandler::dispatch);
            }
        };

        try {
            ctx.addRoutes(builder);
            ctx.start();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void stop() {
        try {
            ctx.stop();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
