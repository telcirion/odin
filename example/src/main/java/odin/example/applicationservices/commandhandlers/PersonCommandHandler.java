/* Copyright 2020 Peter Jansen
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

package odin.example.applicationservices.commandhandlers;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.concepts.applicationservices.ICommandHandler;
import odin.concepts.applicationservices.IRepository;
import odin.concepts.common.IMessage;
import odin.concepts.common.IMessageHandler;
import odin.concepts.common.Identity;
import odin.concepts.domainmodel.ICommand;
import odin.example.domain.commands.ChangePersonName;
import odin.example.domain.commands.RegisterPerson;
import odin.example.domain.state.Person;
import odin.framework.common.Matcher;
import odin.framework.domainmodel.Aggregate;

public class PersonCommandHandler implements ICommandHandler {

    private final IRepository personRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public PersonCommandHandler(IRepository personRepository) {
        this.personRepository = personRepository;
    }

    private ICommandHandler handle(RegisterPerson registerPerson) {
        this.log(registerPerson);
        var p = new Aggregate<>(new Identity(), new Person());
        p.process(registerPerson);
        personRepository.save(p);
        LOGGER.info("Person with name {} and ssn {} registered.", p.getAggrateRoot().getName(),
                p.getAggrateRoot().getSsn());
        return this;
    }

    private ICommandHandler handle(ChangePersonName changePersonName) {
        this.log(changePersonName);
        var p = personRepository.load(new Aggregate<>(changePersonName.getMessageInfo().getSubjectId(), new Person()));
        p.process(changePersonName);
        personRepository.save(p);
        return this;
    }

    @Override
    public IMessageHandler handle(IMessage msg) {
        return new Matcher<ICommandHandler>(this).match(RegisterPerson.class, this::handle, msg)
                .match(ChangePersonName.class, this::handle, msg).result();
    }

    private void log(ICommand command) {
        LOGGER.info("Command {} received for aggregateId: {}, revision: {}", command.getClass().getSimpleName(),
                command.getMessageInfo().getSubjectId(), command.getMessageInfo().geSubjectVersion());
    }
}
