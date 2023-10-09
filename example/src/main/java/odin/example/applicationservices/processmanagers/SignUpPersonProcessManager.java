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

import odin.applicationservices.ProcessManager;
import odin.common.Identity;
import odin.common.Message;
import odin.common.MessageDispatcher;
import odin.common.MessageHandler;
import odin.common.SendMessage;
import odin.example.domain.commands.RegisterPerson;
import odin.example.domain.events.PersonRegistered;
import odin.example.domain.events.PersonSignUpReceived;

public class SignUpPersonProcessManager implements ProcessManager {

    private final SendMessage commandBus;

    final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SignUpPersonProcessManager(SendMessage commandBus) {
        this.commandBus = commandBus;
    }

    @Override
    public MessageHandler handle(Message msg) {
        return new MessageDispatcher<MessageHandler>(this).match(PersonSignUpReceived.class, this::handle, msg)
                .match(PersonRegistered.class, this::handle, msg).result();
    }

    private MessageHandler handle(PersonSignUpReceived msg) {
        logReception(msg);
        commandBus.send(new RegisterPerson(new Identity(), msg.getLastName(), msg.getFirstName()));
        return this;
    }

    private MessageHandler handle(PersonRegistered msg) {
        logReception(msg);
        return this;
    }

    private void logReception(Message msg) {
        logger.info("Event {} received, on {}.", msg.getClass().getSimpleName(), msg.getMessageInfo().timestamp());
    }
}
