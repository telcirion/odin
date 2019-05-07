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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cqrs.concepts.applicationservices.ICommand;
import cqrs.concepts.applicationservices.IMessageBusFactory;
import cqrs.concepts.applicationservices.IProcessManager;
import cqrs.concepts.applicationservices.ISendMessage;
import cqrs.concepts.common.IDispatcher;
import cqrs.concepts.common.IMessageHandler;
import cqrs.framework.DispatcherBuilder;
import cqrs.test.applicationservices.commands.RegistreerPersoon;
import cqrs.test.domain.events.PersoonAangemeld;
import cqrs.test.domain.events.PersoonGeregistreerd;

public class AanmeldingProcessManager implements IProcessManager {
	private final String outboundEndpoint;
    private final IMessageBusFactory messageBusFactory;
	public AanmeldingProcessManager(String outboundEndpoint, IMessageBusFactory messageBusFactory){
		this.outboundEndpoint=outboundEndpoint;
		this.messageBusFactory=messageBusFactory;
	}

    @Override
	public IDispatcher getDispatcher() {
		return new DispatcherBuilder()
				.dispatch(PersoonAangemeld.class, this::handle)
				.dispatch(PersoonGeregistreerd.class, this::handle)
				.build();
	}

	private IMessageHandler handle(PersoonAangemeld msg){
		final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
		logger.info("Event " +msg.getClass().getSimpleName() + " ontvangen, command naar: "+outboundEndpoint+".");
		ISendMessage<ICommand> bus1=messageBusFactory.getMessageBus(outboundEndpoint);
		bus1.send(new RegistreerPersoon(UUID.randomUUID(), msg.getSsn(),msg.getNaam()));
		return this;
	}
	
	private IMessageHandler handle(PersoonGeregistreerd msg){
		final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
		logger.info("Event " +msg.getClass().getSimpleName() + " ontvangen.");
		logger.info("Message aggregateId: "+ msg.getAggregateId()
				+ " message ssn value: "+ msg.getSsn()
				+ " message name value: "+ msg.getNaam());
		return this;
	}
}
