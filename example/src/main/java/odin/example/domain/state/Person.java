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

package odin.example.domain.state;

import odin.concepts.domainmodel.AggregateRoot;
import odin.concepts.domainmodel.Command;
import odin.concepts.domainmodel.DomainEvent;
import odin.example.domain.commands.ChangePersonName;
import odin.example.domain.commands.RegisterPerson;
import odin.example.domain.events.PersonNameChanged;
import odin.example.domain.events.PersonRegistered;
import odin.framework.common.MessageDispatcher;

public class Person implements AggregateRoot {

    private String firstName;
    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Person() {
        this.firstName = null;
        this.lastName = null;
    }

    private Person registered(final PersonRegistered event) {
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        return this;
    }

    private Person changedName(final PersonNameChanged event) {
        this.firstName = event.getFirstName();
        return this;
    }

    public DomainEvent register(RegisterPerson command) {
        return new PersonRegistered(command.getMessageInfo().subjectId(), command.getSsn(), command.getName());
    }

    public DomainEvent changeName(ChangePersonName command) {
        return new PersonNameChanged(command.getMessageInfo().subjectId(), command.getName());
    }

    @Override
    public DomainEvent process(Command command) {
        var event = new MessageDispatcher<DomainEvent>(null).match(RegisterPerson.class, this::register, command)
                .match(ChangePersonName.class, this::changeName, command).result();
        source(event);
        return event;
    }

    @Override
    public AggregateRoot source(DomainEvent msg) {
        return new MessageDispatcher<>(this).match(PersonRegistered.class, this::registered, msg)
                .match(PersonNameChanged.class, this::changedName, msg).result();

    }
}
