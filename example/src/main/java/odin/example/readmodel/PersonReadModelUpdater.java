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

package odin.example.readmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import odin.applicationservices.ReadModelUpdater;
import odin.common.Message;
import odin.common.MessageDispatcher;
import odin.common.MessageHandler;
import odin.domainmodel.DomainEvent;
import odin.example.domain.events.PersonNameChanged;
import odin.example.domain.events.PersonRegistered;

@Service
public class PersonReadModelUpdater implements ReadModelUpdater<PersonReadModelRepository> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonReadModelUpdater.class);
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

    @Autowired
    private PersonReadModelRepository personList;

    private ReadModelUpdater<PersonReadModelRepository> handle(PersonRegistered personRegistered) {
        this.log(personRegistered);
        PersistableReadModelPerson person = new PersistableReadModelPerson(null,
                personRegistered.getMessageInfo().subjectId(), personRegistered.getFirstName(),
                personRegistered.getLastName());
        personList.save(person);
        synchronized (this) {
            numberOfPersonRegisteredReceived++;
        }
        return this;
    }

    private ReadModelUpdater<PersonReadModelRepository> handle(PersonNameChanged personNameChanged) {
        this.log(personNameChanged);

        PersistableReadModelPerson person = personList.findByIdentity(personNameChanged.getMessageInfo().subjectId());
        person.setFirstName(personNameChanged.getFirstName());
        personList.save(person);
        synchronized (this) {
            numberOfPersonNameChangedReceived++;
        }
        return this;
    }

    @Override
    public MessageHandler handle(Message msg) {
        return new MessageDispatcher<MessageHandler>(this).match(PersonRegistered.class, this::handle, msg)
                .match(PersonNameChanged.class, this::handle, msg).result();
    }

    private void log(DomainEvent event) {
        LOGGER.info("DomainEvent {} received for aggregateId: {}", event.getClass().getSimpleName(),
                event.getMessageInfo().subjectId());
    }

    @Override
    public PersonReadModelRepository getReadModelRepository() {
        return personList;
    }
}
