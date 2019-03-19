package cqrs.test;

import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import org.junit.jupiter.api.Test;

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
		StatusLogger.getLogger().setLevel(Level.OFF);

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
		System.out.println("ProcessManager aangemaakt, wacht op verwerking.");

		//start commandhandler
		new ProcessorHost(COMMANDQUEUE,dir);
		System.out.println("CommandHandler aangemaakt, wacht op verwerking.");
		
		//stuur eerste event
		bus1.send(new PersoonAangemeld("1234567892","pietje"));
		bus1.send(new PersoonAangemeld("1234567893","jantje"));

		System.out.println("Domainevent verstuurd.");
		
		//na twee events zijn we klaar
		//noinspection StatementWithEmptyBody
		while (h.getCalled()<4);
		System.out.println("alle domainevents verwerkt.");

		assertTrue(true);
		H2DBServer.stopServer();
	}
}
