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

package odin.example.applicationservices.denormalizers;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.concepts.applicationservices.IDenormalizer;
import odin.concepts.common.IMessage;
import odin.concepts.common.IMessageHandler;
import odin.concepts.domainmodel.IDomainEvent;
import odin.example.domain.events.PersonNameChanged;
import odin.example.domain.events.PersonRegistered;
import odin.example.readmodel.Person;
import odin.example.readmodel.PersonList;
import odin.framework.common.Matcher;

public class PersonDenormalizer implements IDenormalizer<PersonList> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private int numberOfPersonRegisteredReceived = 0;
    private int numberOfPersonNameChangedReceived = 0;

    public int getNumberOfPersonRegisteredReceived() {
        synchronized (this) {
            return numberOfPersonRegisteredReceived;
        }
    }

    public int getNumberOfPersonNameChangedReceived() {
        synchronized (this) {
            return numberOfPersonNameChangedReceived;
        }
    }

    private final PersonList personList = new PersonList();

    private IDenormalizer<PersonList> handle(PersonRegistered personRegistered) {
        this.log(personRegistered);
        Person person = new Person(personRegistered.getMessageInfo().getSubjectId(), personRegistered.getName(),
                personRegistered.getSsn());
        personList.add(person);
        synchronized (this) {
            numberOfPersonRegisteredReceived++;
        }
        return this;
    }

    private IDenormalizer<PersonList> handle(PersonNameChanged personNameChanged) {
        this.log(personNameChanged);
        Person person = new Person(personNameChanged.getMessageInfo().getSubjectId(), personNameChanged.getName(),
                null);
        personList.updateName(person);
        synchronized (this) {
            numberOfPersonNameChangedReceived++;
        }
        return this;
    }

    @Override
    public IMessageHandler handle(IMessage msg) {
        return new Matcher<IMessageHandler>(this).match(PersonRegistered.class, this::handle, msg)
                .match(PersonNameChanged.class, this::handle, msg).result();
    }

    private void log(IDomainEvent event) {
        LOGGER.info("DomainEvent {} received for aggregateId: {}", event.getClass().getSimpleName(),
                event.getMessageInfo().getSubjectId());
    }

    @Override
    public PersonList getReadModel() {
        return personList;
    }
}
