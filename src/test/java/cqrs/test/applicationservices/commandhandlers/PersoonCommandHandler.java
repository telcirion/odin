package cqrs.test.applicationservices.commandhandlers;

import cqrs.concepts.applicationservices.ICommandHandler;
import cqrs.concepts.applicationservices.IMessageBusFactory;
import cqrs.concepts.applicationservices.IRepository;
import cqrs.concepts.applicationservices.IRepositoryFactory;
import cqrs.concepts.applicationservices.ISendMessage;
import cqrs.concepts.common.IDispatcher;
import cqrs.concepts.common.IMessageHandler;
import cqrs.concepts.domainmodel.IDomainEvent;
import cqrs.framework.DispatcherBuilder;
import cqrs.framework.SimpleMessageBus;
import cqrs.test.applicationservices.commands.RegistreerPersoon;
import cqrs.test.applicationservices.commands.WijzigNaamPersoon;
import cqrs.test.domain.state.Persoon;

public class PersoonCommandHandler implements ICommandHandler {
    private final String outboundEndpoint;
    private final IRepositoryFactory repositoryFactory;
    private final IMessageBusFactory messageBusFactory;

    public PersoonCommandHandler(String outboundEndpoint, IMessageBusFactory messageBusFactory, IRepositoryFactory repositoryFactory) {
        this.outboundEndpoint = outboundEndpoint;
        this.repositoryFactory = repositoryFactory;
        this.messageBusFactory = messageBusFactory;
    }

    private IMessageHandler handle(RegistreerPersoon registreerPersoon) {
        System.out.println("Command " + registreerPersoon.getClass().getSimpleName() + " ontvangen voor aggregateId:"
                + registreerPersoon.getTargetId() + ", event naar: " + outboundEndpoint);

        ISendMessage<IDomainEvent> bus1 =messageBusFactory.getMessageBus(outboundEndpoint);

        IRepository<Persoon> persoonRepository = repositoryFactory.getRepository();
        var persoon = new Persoon(registreerPersoon.getTargetId())
                .RegistreerPersoon(registreerPersoon.getSsn(), registreerPersoon.getNaam());
        persoonRepository.create(persoon);
        persoon.getEvents().forEach(s -> {
            bus1.send(s);
            System.out.println("EventId: " + s.getEventId());
        });
        return this;
    }

    private IMessageHandler handle(WijzigNaamPersoon wijzigNaamPersoon) {
        System.out.println("Command " + wijzigNaamPersoon.getClass().getSimpleName() + " ontvangen voor aggregateId:"
                + wijzigNaamPersoon.getTargetId() + ", event naar: " + outboundEndpoint);

        ISendMessage<IDomainEvent> bus1 = new SimpleMessageBus<>(outboundEndpoint);

        IRepository<Persoon> persoonRepository = repositoryFactory.getRepository();
        Persoon persoon = persoonRepository.get(new Persoon(wijzigNaamPersoon.getTargetId()))
                .WijzigNaam(wijzigNaamPersoon.getNaam());
        persoonRepository.update(persoon);
        persoon.getEvents().forEach(s -> {
            bus1.send(s);
            System.out.println("EventId: " + s.getEventId());
        });
        return this;
    }

    @Override
    public IDispatcher getDispatcher() {
        return new DispatcherBuilder()
                .dispatch(RegistreerPersoon.class, this::handle)
                .dispatch(WijzigNaamPersoon.class, this::handle)
                .build();
    }

}
