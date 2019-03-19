package cqrs.test.applicationservices.processmanagers;

import java.util.UUID;

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
		System.out.println("Event " +msg.getClass().getSimpleName() + " ontvangen, command naar: "+outboundEndpoint+".");
		ISendMessage<ICommand> bus1=messageBusFactory.getMessageBus(outboundEndpoint);
		bus1.send(new RegistreerPersoon(UUID.randomUUID(), msg.getSsn(),msg.getNaam()));
		return this;
	}
	
	private IMessageHandler handle(PersoonGeregistreerd msg){
		System.out.println("Event " +msg.getClass().getSimpleName() + " ontvangen.");
		System.out.println("Message aggregateId: "+ msg.getAggregateId()
				+ " message ssn value: "+ msg.getSsn()
				+ " message name value: "+ msg.getNaam());
		return this;
	}
}
