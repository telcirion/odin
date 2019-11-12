/* Copyright 2019 Peter Jansen
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

package odin.test.applicationservices.commandhandlers;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.concepts.applicationservices.ICommand;
import odin.concepts.applicationservices.ICommandHandler;
import odin.concepts.applicationservices.IRepository;
import odin.concepts.common.IMessageHandler;
import odin.concepts.domainmodel.ISendDomainEvent;
import odin.test.applicationservices.commands.ChangePersonName;
import odin.test.applicationservices.commands.RegisterPerson;
import odin.test.domain.state.Person;

public class PersonCommandHandler implements ICommandHandler {

    private final IRepository<Person> personRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public PersonCommandHandler(IRepository<Person> personRepository) {
        this.personRepository = personRepository;
    }

    private ICommandHandler handle(RegisterPerson registerPerson) {
        this.log(registerPerson);

        new Person(registerPerson.getTargetId(), (ISendDomainEvent)personRepository)
                .registerPerson(registerPerson.getSsn(), registerPerson.getName());
        return this;
    }

    private ICommandHandler handle(ChangePersonName changePersonName) {
        this.log(changePersonName);
        personRepository.get(new Person(changePersonName.getTargetId(), (ISendDomainEvent)personRepository))
                .changeName(changePersonName.getName());
        return this;
    }

    @Override
    public <T, Z extends IMessageHandler> Z getDispatcher(T msg) {
        return match(RegisterPerson.class, this::handle, msg).match(ChangePersonName.class, this::handle, msg);

    }

    private void log(ICommand command) {
        LOGGER.info("Command {} received for aggregateId: {}, revision: {}", command.getClass().getSimpleName(),
                command.getTargetId(), command.getTargetVersion());
    }
}
