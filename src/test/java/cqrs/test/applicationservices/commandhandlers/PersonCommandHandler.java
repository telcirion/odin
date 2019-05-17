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

import cqrs.concepts.applicationservices.*;
import cqrs.concepts.common.IMessageHandler;
import cqrs.concepts.domainmodel.IDomainEvent;
import cqrs.framework.AbstractMessageHandler;
import cqrs.test.applicationservices.commands.ChangePersonName;
import cqrs.test.applicationservices.commands.RegisterPerson;
import cqrs.test.domain.state.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class PersonCommandHandler extends AbstractMessageHandler implements ICommandHandler {

    private final IRepositoryFactory repositoryFactory;
    private final ISendMessage<IDomainEvent> messageBus;

    public PersonCommandHandler(ISendMessage<IDomainEvent> messageBus, IRepositoryFactory repositoryFactory) {
        this.messageBus=messageBus;
        this.repositoryFactory = repositoryFactory;
    }

    private IMessageHandler handle(RegisterPerson registerPerson) {
        final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
        logger.info("Command " + registerPerson.getClass().getSimpleName() + " received for aggregateId:"
                + ((ICommand)registerPerson).getTargetId());

        IRepository<Person> personRepository = repositoryFactory.getRepository();
        var person = new Person(registerPerson.getTargetId())
                .registerPerson(registerPerson.getSsn(), registerPerson.getName());
        personRepository.create(person);
        person.getEvents().forEach(s -> {
            messageBus.send(s);
            logger.info("EventId: " + s.getEventId());
        });
        return this;
    }

    private IMessageHandler handle(ChangePersonName changePersonName) {
        final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
        logger.info("Command " + changePersonName.getClass().getSimpleName() + " received for aggregateId:"
                + changePersonName.getTargetId()
                + ", revision: " + ((ICommand)changePersonName).getTargetVersion());

        IRepository<Person> personRepository = repositoryFactory.getRepository();
        Person person = personRepository.get(new Person(changePersonName.getTargetId()))
                .changeName(changePersonName.getName());
        personRepository.update(person);
        person.getEvents().forEach(s -> {
            messageBus.send(s);
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
