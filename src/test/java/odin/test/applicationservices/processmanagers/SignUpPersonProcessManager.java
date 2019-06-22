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
package odin.test.applicationservices.processmanagers;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

import odin.concepts.domainmodel.IDomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.concepts.applicationservices.IProcessManager;
import odin.concepts.applicationservices.ISendMessage;
import odin.concepts.common.IMessageHandler;
import odin.test.applicationservices.commands.RegisterPerson;
import odin.test.domain.events.PersonSignUpReceived;
import odin.test.domain.events.PersonRegistered;

public class SignUpPersonProcessManager implements IProcessManager {

    private final ISendMessage commandBus;
	
	final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public SignUpPersonProcessManager(ISendMessage commandBus){
		this.commandBus=commandBus;
	}

    @Override
	public <T,Z extends IMessageHandler> Z getDispatcher(T msg){
		return match(PersonSignUpReceived.class, this::handle, msg)
				.match(PersonRegistered.class, this::handle,msg);
	}

	private IMessageHandler handle(PersonSignUpReceived msg){
		logger.info("Event " +msg.getClass().getSimpleName() + " received");

		commandBus.send(new RegisterPerson(UUID.randomUUID(), msg.getSsn(),msg.getName()));
		return this;
	}
	
	private IMessageHandler handle(PersonRegistered msg){
		logger.info("Event " +msg.getClass().getSimpleName() + " received.");
		logger.info("Message aggregateId: "+ ((IDomainEvent)msg).getAggregateId()
				+ " message ssn value: "+ msg.getSsn()
				+ " message name value: "+ msg.getName());

		return this;
	}
}
