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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.concepts.common.IMessage;
import odin.concepts.common.IMessageHandler;
import odin.concepts.common.IPublishMessage;
import odin.concepts.common.ISendMessage;

public class SimplePubSub implements ISendMessage, IPublishMessage {
    final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final List<IMessageHandler> subscribers = new ArrayList<>();

    @Override
    public void send(IMessage m) {
        subscribers.forEach(x -> x.handle(m));
    }

    @Override
    public void subscribe(IMessageHandler messageHandler) {
        subscribers.add(messageHandler);
    }
}
