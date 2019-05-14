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

package cqrs.test.applicationservices.commandhandlers;

import java.lang.invoke.MethodHandles;

import cqrs.concepts.applicationservices.*;
import cqrs.framework.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cqrs.concepts.common.IMessageHandler;
import cqrs.concepts.domainmodel.IDomainEvent;
import cqrs.framework.SimpleMessageBus;
import cqrs.test.applicationservices.commands.RegisterPerson;
import cqrs.test.applicationservices.commands.ChangePersonName;
import cqrs.test.domain.state.Person;

public class PersonCommandHandler extends AbstractMessageHandler implements ICommandHandler {
    private final String outboundEndpoint;
    private final IRepositoryFactory repositoryFactory;
    private final IMessageBusFactory messageBusFactory;

    public PersonCommandHandler(String outboundEndpoint, IMessageBusFactory messageBusFactory, IRepositoryFactory repositoryFactory) {
        this.outboundEndpoint = outboundEndpoint;
        this.repositoryFactory = repositoryFactory;
        this.messageBusFactory = messageBusFactory;
    }

    private IMessageHandler handle(RegisterPerson registerPerson) {
        final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
        logger.info("Command " + registerPerson.getClass().getSimpleName() + " received for aggregateId:"
                + ((ICommand)registerPerson).getTargetId() + ", event to: " + outboundEndpoint);

        ISendMessage<IDomainEvent> bus1 =messageBusFactory.getMessageBus(outboundEndpoint);

        IRepository<Person> personRepository = repositoryFactory.getRepository();
        var person = new Person(registerPerson.getTargetId())
                .registerPerson(registerPerson.getSsn(), registerPerson.getName());
        personRepository.create(person);
        person.getEvents().forEach(s -> {
            bus1.send(s);
            logger.info("EventId: " + s.getEventId());
        });
        return this;
    }

    private IMessageHandler handle(ChangePersonName changePersonName) {
        final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
        logger.info("Command " + changePersonName.getClass().getSimpleName() + " received for aggregateId:"
                + changePersonName.getTargetId()
                + ", revision: " + ((ICommand)changePersonName).getTargetVersion(),
                ", event to: " + outboundEndpoint);

        ISendMessage<IDomainEvent> bus1 = new SimpleMessageBus<>(outboundEndpoint);

        IRepository<Person> personRepository = repositoryFactory.getRepository();
        Person person = personRepository.get(new Person(changePersonName.getTargetId()))
                .changeName(changePersonName.getName());
        personRepository.update(person);
        person.getEvents().forEach(s -> {
            bus1.send(s);
            logger.info("EventId: " + s.getEventId());
        });
        return this;
    }


    @Override
    public <T,Z extends IMessageHandler> Z getDispatcher2(T msg) {
        return match(RegisterPerson.class, this::handle, msg)
                .match(ChangePersonName.class, this::handle, msg);

    }


}
