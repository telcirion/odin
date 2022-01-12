/* Copyright 2020 Peter Jansen
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

package odin.example.applicationservices.processmanagers;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.concepts.applicationservices.IProcessManager;
import odin.concepts.common.IMessage;
import odin.concepts.common.IMessageHandler;
import odin.concepts.common.ISendMessage;
import odin.concepts.common.Identity;
import odin.example.domain.commands.RegisterPerson;
import odin.example.domain.events.PersonRegistered;
import odin.example.domain.events.PersonSignUpReceived;
import odin.framework.common.Dispatcher;

public class SignUpPersonProcessManager implements IProcessManager {

    private final ISendMessage commandBus;

    final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SignUpPersonProcessManager(ISendMessage commandBus) {
        this.commandBus = commandBus;
    }

    @Override
    public IMessageHandler handle(IMessage msg) {
        return new Dispatcher<IMessageHandler>(this).match(PersonSignUpReceived.class, this::handle, msg)
                .match(PersonRegistered.class, this::handle, msg).result();
    }

    private IMessageHandler handle(PersonSignUpReceived msg) {
        logReception(msg);
        commandBus.send(new RegisterPerson(new Identity(), msg.getSsn(), msg.getName()));
        return this;
    }

    private IMessageHandler handle(PersonRegistered msg) {
        logReception(msg);
        return this;
    }

    private void logReception(IMessage msg) {
        logger.info("Event {} received, on {}.", msg.getClass().getSimpleName(), msg.getMessageInfo().timestamp());
    }
}
