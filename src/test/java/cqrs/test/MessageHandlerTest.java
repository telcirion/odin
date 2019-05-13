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

package cqrs.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.invoke.MethodHandles;

import cqrs.test.applicationservices.queries.PersonByNameQuery;
import cqrs.test.applicationservices.queryhandlers.PersonQueryHandler;
import cqrs.test.applicationservices.queryresults.PersonQueryResult;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cqrs.concepts.applicationservices.IMessageBusFactory;
import cqrs.concepts.applicationservices.IRepository;
import cqrs.concepts.applicationservices.IRepositoryFactory;
import cqrs.concepts.applicationservices.ISendMessage;
import cqrs.concepts.domainmodel.IAggregateRoot;
import cqrs.concepts.domainmodel.IDomainEvent;
import cqrs.concepts.infra.IDirectory;
import cqrs.concepts.infra.IProcessorHost;
import cqrs.framework.Directory;
import cqrs.framework.ProcessorHost;
import cqrs.framework.SimpleMessageBus;
import cqrs.infrastructure.H2DBServer;
import cqrs.infrastructure.SQLEventStore;
import cqrs.test.applicationservices.commandhandlers.PersonCommandHandler;
import cqrs.test.applicationservices.processmanagers.SignUpPersonProcessManager;
import cqrs.test.domain.events.PersonSignUpReceived;

class MessageHandlerTest {

	private static final String EVENT_QUEUE ="vm:eventQueue";
	private static final String COMMAND_QUEUE ="vm:commandQueue";

	@Test
	void test() {
		final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

		H2DBServer.startServer();
		if(H2DBServer.isRunning()){
			logger.info("Database seems to be running.");
		} else {
			logger.info("Database seems not to be running.");
		}

		IRepositoryFactory repositoryFactory=  new IRepositoryFactory() {
			@Override
			public <T extends IAggregateRoot<T>> IRepository<T> getRepository() {
				return new SQLEventStore<>(new TestDataSource());
			}
		};

		IMessageBusFactory messageBusFactory= SimpleMessageBus::new;

		ISendMessage<IDomainEvent> eventBus=new SimpleMessageBus<>(EVENT_QUEUE);
		IDirectory dir=new Directory();
		dir.registerHandler(new SignUpPersonProcessManager(COMMAND_QUEUE, messageBusFactory));
		dir.registerHandler(new PersonCommandHandler(EVENT_QUEUE, messageBusFactory, repositoryFactory));
		
		//create db
		new SQLEventStore<>(new TestDataSource()).createDatabase();

		//start processManager
		IProcessorHost processorHost=new ProcessorHost(EVENT_QUEUE,dir);
		logger.info("ProcessManager created, wait for processing.");

		//start commandHandler
		new ProcessorHost(COMMAND_QUEUE,dir);
		logger.info("CommandHandler created, wait for processing.");
		
		//send first event
		eventBus.send(new PersonSignUpReceived("1234567892","Peter"));
		eventBus.send(new PersonSignUpReceived("1234567893","John"));

		logger.info("DomainEvent send.");
		
		//after processing 4 events, we're done.
		//noinspection StatementWithEmptyBody
		while (processorHost.getCalled()<4);
		logger.info("All DomainEvents were processed.");

		//let's try a query
		PersonQueryHandler queryHandler=new PersonQueryHandler();
		PersonQueryResult personQueryResult=queryHandler.query(new PersonByNameQuery("no name"));
		if(personQueryResult !=null){
			logger.info("Person found with name: "+personQueryResult.getPerson().getName() +
					" and ssn: " + personQueryResult.getPerson().getSsn());
		}
		assertTrue(true);
		H2DBServer.stopServer();
	}
}
