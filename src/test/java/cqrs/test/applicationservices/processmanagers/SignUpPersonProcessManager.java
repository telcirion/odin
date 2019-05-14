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

package cqrs.test.applicationservices.processmanagers;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

import cqrs.concepts.domainmodel.IDomainEvent;
import cqrs.framework.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cqrs.concepts.applicationservices.ICommand;
import cqrs.concepts.applicationservices.IMessageBusFactory;
import cqrs.concepts.applicationservices.IProcessManager;
import cqrs.concepts.applicationservices.ISendMessage;
import cqrs.concepts.common.IMessageHandler;
import cqrs.test.applicationservices.commands.RegisterPerson;
import cqrs.test.domain.events.PersonSignUpReceived;
import cqrs.test.domain.events.PersonRegistered;

public class SignUpPersonProcessManager extends AbstractMessageHandler implements IProcessManager {
	private final String outboundEndpoint;
    private final IMessageBusFactory messageBusFactory;
	public SignUpPersonProcessManager(String outboundEndpoint, IMessageBusFactory messageBusFactory){
		this.outboundEndpoint=outboundEndpoint;
		this.messageBusFactory=messageBusFactory;
	}

    @Override
	public <T,Z extends IMessageHandler> Z getDispatcher2(T msg){
		return match(PersonSignUpReceived.class, this::handle, msg)
				.match(PersonRegistered.class, this::handle,msg);
	}

	private IMessageHandler handle(PersonSignUpReceived msg){
		final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
		logger.info("Event " +msg.getClass().getSimpleName() + " received, command to: "+outboundEndpoint+".");
		ISendMessage<ICommand> bus1=messageBusFactory.getMessageBus(outboundEndpoint);
		bus1.send(new RegisterPerson(UUID.randomUUID(), msg.getSsn(),msg.getName()));
		return this;
	}
	
	private IMessageHandler handle(PersonRegistered msg){
		final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
		logger.info("Event " +msg.getClass().getSimpleName() + " received.");
		logger.info("Message aggregateId: "+ ((IDomainEvent)msg).getAggregateId()
				+ " message ssn value: "+ msg.getSsn()
				+ " message name value: "+ msg.getName());
		return this;
	}
}
