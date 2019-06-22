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

import odin.concepts.applicationservices.ICommand;
import odin.concepts.applicationservices.ICommandHandler;
import odin.concepts.applicationservices.IRepository;
import odin.concepts.applicationservices.ISendMessage;
import odin.concepts.common.IMessageHandler;
import odin.test.applicationservices.commands.ChangePersonName;
import odin.test.applicationservices.commands.RegisterPerson;
import odin.test.domain.state.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class PersonCommandHandler implements ICommandHandler {

    private final ISendMessage messageBus;
    private final IRepository<Person> personRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public PersonCommandHandler(ISendMessage messageBus, IRepository<Person> personRepository) {
        this.messageBus=messageBus;
        this.personRepository=personRepository;
    }

    private ICommandHandler handle(RegisterPerson registerPerson) {
        this.log(registerPerson);

        var person = new Person(registerPerson.getTargetId())
                .registerPerson(registerPerson.getSsn(), registerPerson.getName());
        personRepository.create(person);
        person.getEvents().forEach(messageBus::send);
        return this;
    }

    private ICommandHandler handle(ChangePersonName changePersonName) {
        this.log(changePersonName);
        Person person = personRepository.get(new Person(changePersonName.getTargetId()))
                .changeName(changePersonName.getName());
        personRepository.update(person);
        person.getEvents().forEach(messageBus::send);
        return this;
    }

    @Override
    public <T,Z extends IMessageHandler> Z getDispatcher(T msg) {
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
