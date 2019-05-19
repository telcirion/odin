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
import cqrs.concepts.common.IMessageHandler;
import cqrs.concepts.domainmodel.IDomainEvent;
import cqrs.framework.AbstractMessageHandler;
import cqrs.test.applicationservices.commands.ChangePersonName;
import cqrs.test.applicationservices.commands.RegisterPerson;
import cqrs.test.domain.state.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

// TODO add some documentation on this class,
// e.g. why does it implement commandhandler, what can you use it for?
// especially important becuase it looks like it is an example implementation.

public class PersonCommandHandler extends AbstractMessageHandler implements ICommandHandler {

    private final IRepositoryFactory repositoryFactory;
    private final ISendMessage<IDomainEvent> messageBus;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public PersonCommandHandler(ISendMessage<IDomainEvent> messageBus, IRepositoryFactory repositoryFactory) {
        this.messageBus=messageBus;
        this.repositoryFactory = repositoryFactory;
    }

    private IMessageHandler handle(RegisterPerson registerPerson) {
        this.log(registerPerson);

        var person = new Person(registerPerson.getTargetId())
                .registerPerson(registerPerson.getSsn(), registerPerson.getName());
        personRepository.create(person);
        person.getEvents().forEach(s -> messageBus.send(s));
        return this;
    }

    private IMessageHandler handle(ChangePersonName changePersonName) {
        // I reference the log method on `this` instance,
        // such that the reader always understands what kind of method is being used
        this.log(changePersonName);
        IRepository<Person> personRepository = repositoryFactory.getRepository();
        Person person = personRepository.get(new Person(changePersonName.getTargetId()))
                .changeName(changePersonName.getName());
        personRepository.update(person);
        person.getEvents().forEach(s -> messageBus.send(s));
        return this;
    }


    @Override
    public <T,Z extends IMessageHandler> Z getDispatcher2(T msg) {
        return match(RegisterPerson.class, this::handle, msg)
                .match(ChangePersonName.class, this::handle, msg);

    }
    private void log(ICommand command) {
        LOGGER.info(
                "Command {} received for aggregateId: {}, revision: {}",
                command.getClass().getSimpleName(),
                command.getTargetId(),
                command.getTargetVersion()
        );
    }
}
