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
import cqrs.test.applicationservices.commandhandlers.PersoonCommandHandler;
import cqrs.test.applicationservices.processmanagers.AanmeldingProcessManager;
import cqrs.test.domain.events.PersoonAangemeld;

class MessageHandlerTest {

	private static final String EVENTQUEUE ="vm:eventqueue";
	private static final String COMMANDQUEUE ="vm:commandqueue";

	@Test
	void test() {
		final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

		H2DBServer.startServer();
		IRepositoryFactory f=  new IRepositoryFactory() {
			@Override
			public <T extends IAggregateRoot<T>> IRepository<T> getRepository() {
				return new SQLEventStore<>(new HikariCPTestDataSource());
			}
		};

		IMessageBusFactory messageBusFactory= SimpleMessageBus::new;

		ISendMessage<IDomainEvent> bus1=new SimpleMessageBus<>(EVENTQUEUE);
		IDirectory dir=new Directory();
		dir.registerHandler(new AanmeldingProcessManager(COMMANDQUEUE, messageBusFactory));
		dir.registerHandler(new PersoonCommandHandler(EVENTQUEUE, messageBusFactory, f));
		
		//create db
		SQLEventStore.createDatabase(new HikariCPTestDataSource());

		//start processmanager
		IProcessorHost h=new ProcessorHost(EVENTQUEUE,dir);
		logger.info("ProcessManager aangemaakt, wacht op verwerking.");

		//start commandhandler
		new ProcessorHost(COMMANDQUEUE,dir);
		logger.info("CommandHandler aangemaakt, wacht op verwerking.");
		
		//stuur eerste event
		bus1.send(new PersoonAangemeld("1234567892","pietje"));
		bus1.send(new PersoonAangemeld("1234567893","jantje"));

		logger.info("Domainevent verstuurd.");
		
		//na twee events zijn we klaar
		//noinspection StatementWithEmptyBody
		while (h.getCalled()<4);
		logger.info("alle domainevents verwerkt.");

		assertTrue(true);
		H2DBServer.stopServer();
	}
}
