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

package odin.framework.infrastructure;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.concepts.common.IMessage;
import odin.concepts.common.IMessageHandler;
import odin.concepts.common.IPublishMessage;
import odin.concepts.common.ISendMessage;

public class SimpleMessageBus implements ISendMessage, IPublishMessage {
    final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String endpoint;
    private final CamelContext ctx;

    public enum BusType {
        TOPIC("?multipleConsumers=true"), QUEUE("");

        private final String type;

        public String getTypeString() {
            return type;
        }

        private BusType(String type) {
            this.type = type;
        }
    }

    public SimpleMessageBus(BusType busType) {
        ctx = new DefaultCamelContext();
        ctx.setStreamCaching(true);
        this.endpoint = new StringBuilder().append("vm:").append(UUID.randomUUID().toString())
            .append(busType.getTypeString()).toString();
    }

    @Override
    public void send(IMessage m) {
        try (ProducerTemplate prodTemp = ctx.createProducerTemplate()) {
            prodTemp.sendBody(endpoint, m);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void subscribe(IMessageHandler messageHandler) {
        RouteBuilder builder = new RouteBuilder() {
            public void configure() {
                errorHandler(deadLetterChannel("mock:error"));
                from(endpoint).process().body(IMessage.class, messageHandler::handle);
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
