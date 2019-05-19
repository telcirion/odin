/*
 * Copyright 2019 Peter Jansen
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

package cqrs.test.applicationservices.commandhandlers;

// TODO: be explicit about imports, don't use `.*`
// see: http://www.javadude.com/posts/20040522-import-on-demand-is-evil/
// Also: long explicit import lists are a code smell.
// Too many class dependencies means it is likely to have too many responsibilities.
// In this case still fine, but being explicit makes it clear when it starts to smell.
import cqrs.concepts.applicationservices.*;
import cqrs.concepts.common.IDispatcher;
import cqrs.concepts.common.IMessageHandler;
import cqrs.concepts.domainmodel.IDomainEvent;
import cqrs.framework.DispatcherBuilder;
import cqrs.framework.SimpleMessageBus;
import cqrs.test.applicationservices.commands.ChangePersonName;
import cqrs.test.applicationservices.commands.RegisterPerson;
import cqrs.test.domain.state.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

// TODO add some documentation on this class,
// e.g. why does it implement commandhandler, what can you use it for?
// especially important becuase it looks like it is an example implementation.
public class PersonCommandHandler implements ICommandHandler {

    // I've moved the logging here, it only needs to be done once, and cluttered the other methods
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // TODO Add some documentation on what these fields are used for.
    private final String outboundEndpoint;
    private final IMessageBusFactory messageBusFactory;
    private final IRepository<Person> personRepo;

    // TODO add documentation on how to instantiate objects of this class
    public PersonCommandHandler(
            String outboundEndpoint,
            IMessageBusFactory messageBusFactory,
            IRepositoryFactory repositoryFactory
            // Now the parameters fit on a screen,
            // even in GitHub (about 120 chars / line; see: https://stackoverflow.com/q/22207920)
    ) {
        this.outboundEndpoint = outboundEndpoint;
        this.messageBusFactory = messageBusFactory;
        // I've moved the person repo creation here, since it cluttered the other methods
        // and I don't think new instances are needed every time.
        this.personRepo = repositoryFactory.getRepository();
    }

    // TODO add documentation on what this method is or does
    // Preferably add that documentation to the interface this class is implementating.
    @Override
    public IDispatcher getDispatcher() {
        return new DispatcherBuilder()
                .dispatch(RegisterPerson.class, this::handle)
                .dispatch(ChangePersonName.class, this::handle)
                .build();
    }

    private IMessageHandler handle(RegisterPerson registerPerson) {
        this.log(registerPerson);

        // I'm not sure why this bus is requested from the bus factory,
        // while the bus in the other `handle` method uses a constructor.
        // Is creating/retrieving the bus, part of this method's responsibility?
        // TODO: if no reason to have multiple ways, refactor to one way and possibly move to a field.
        ISendMessage<IDomainEvent> bus1 = this.messageBusFactory.getMessageBus(this.outboundEndpoint);

        var person = new Person(registerPerson.getTargetId())
                .registerPerson(registerPerson.getSsn(), registerPerson.getName());
        this.personRepo.create(person);
        person.getEvents().forEach(s -> send(bus1, s));
        return this;
    }

    private IMessageHandler handle(ChangePersonName changePersonName) {
        // I reference the log method on `this` instance,
        // such that the reader always understands what kind of method is being used
        // similar to the `getDispatcher` method declaring which `handle` methods to use (this.handle).
        this.log(changePersonName);

        // This is the other way mentioned in the to-do above.
        // TODO remove comment.
        ISendMessage<IDomainEvent> bus1 = new SimpleMessageBus<>(this.outboundEndpoint);

        var person = this.personRepo.get(new Person(changePersonName.getTargetId()))
                .changeName(changePersonName.getName());
        this.personRepo.update(person);
        // TODO: move this next line, it is duplicated in both methods, and likely in all message handlers
        person.getEvents().forEach(s -> send(bus1, s));
        return this;
    }

    // This code was duplicated in both handlers
    private void send(ISendMessage<IDomainEvent> bus1, IDomainEvent event) {
        bus1.send(event);
        LOGGER.info("EventId: {}", event.getEventId());
    }

    // This code was duplicated in both handlers
    private void log(ICommand command) {
        LOGGER.info(
                "Command {} received for aggregateId: {}, revision: {}, event to: {}",
                command.getClass().getSimpleName(),
                command.getTargetId(),
                command.getTargetVersion(),
                this.outboundEndpoint
        );
    }

}
