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

package odin.example.applicationservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.invoke.MethodHandles;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import odin.example.applicationservices.commandhandlers.PersonCommandHandler;
import odin.example.applicationservices.processmanagers.SignUpPersonProcessManager;
import odin.example.domain.commands.ChangePersonName;
import odin.example.domain.events.PersonSignUpReceived;
import odin.example.domain.state.Person;
import odin.example.readmodel.PersonReadModelUpdater;
import odin.infrastructure.EventRepository;
import odin.infrastructure.EventStore;
import odin.infrastructure.SimplePubSub;

@SpringBootTest
class SimpleDomainTest {
    @Autowired
    private EventStore eventStore;
    @Autowired
    private PersonReadModelUpdater personReadModelUpdater;

    @Test
    void test() {
        final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

        // create an eventbus where state changes to the Person domain are published
        final SimplePubSub eventBus = new SimplePubSub();

        // create a commandbus where commands to the Person domain should be posted to
        final SimplePubSub commandBus = new SimplePubSub();

        // start processManager (Saga pattern) which is responsible for the choreography
        // betweem events and commands for the Person domain. This process manager
        // listens to events, and will issue commands to fulfill the saga.
        SignUpPersonProcessManager signUpPersonProcessManager = new SignUpPersonProcessManager(commandBus);
        eventBus.subscribe(signUpPersonProcessManager);
        logger.info("ProcessManager created, wait for processing.");

        // start a listener for Person domainevents, that will create and update a
        // static readmodel based on these events
        eventBus.subscribe(personReadModelUpdater);
        logger.info("PersonReadModelUpdater created, wait for processing.");

        // Initialize eventstore, the eventstore will also publish events reflecting the
        // changes in the Person domain on the eventbus.
        EventRepository<Person> personRepository = new EventRepository<>(eventStore, eventBus);

        // start commandHandler, which listens to the command bus
        commandBus.subscribe(new PersonCommandHandler(personRepository));
        logger.info("CommandHandler created, wait for processing.");

        // send first events, which would typically come from another domain, such as a
        // registration website for club membership. These evetns are handled by the
        // process manager (saga) which whill issue subsequent commands.
        eventBus.send(new PersonSignUpReceived("Taylor", "Roger"));
        eventBus.send(new PersonSignUpReceived("Le Bon", "Simon"));

        logger.info("DomainEvents send.");

        // after processing 2 registrations, eventually the read model would also
        // contain 2 persons.
        while (personReadModelUpdater.getNumberOfPersonRegisteredReceived() < 2) {
            // do nothing, just wait.
        }
        logger.info("All DomainEvents (PersonRegistered) were processed by the de-normalizer.");

        // let's try a person query
        var personQueryResult = personReadModelUpdater.getReadModelRepository().findByFirstName("Roger");
        if (personQueryResult.size() > 0) {
            logger.info("Person found with first name: "
                    + personQueryResult.get(0).getFirstName() + " and last name: "
                    + personQueryResult.get(0).getLastName());

            // and then change the person's name
            commandBus.send(new ChangePersonName("John", personQueryResult.get(0).getIdentity(),
                    null));
        }

        // eventually the read model should be updated, so wait for name to be changed.
        while (personReadModelUpdater.getNumberOfPersonNameChangedReceived() < 1) {
            // do nothing, just wait.
        }
        logger.info("All DomainEvents (PersonNameChanged) were processed by the PersonReadModelUpdater.");

        // try to find the same person again by Identity
        var anotherPersonQueryResult = personReadModelUpdater.getReadModelRepository()
                .findByIdentity(personQueryResult.get(0).getIdentity());
        if (anotherPersonQueryResult != null) {
            logger.info("Person found with first name: "
                    + anotherPersonQueryResult.getFirstName() + " and last name: "
                    + anotherPersonQueryResult.getLastName());
        }
        eventBus.stop();
        commandBus.stop();

        assertNotNull(anotherPersonQueryResult);
        assertEquals("John", anotherPersonQueryResult.getFirstName());
        // Roger should no longer be found in the read model
        assertEquals(0, personReadModelUpdater.getReadModelRepository().findByFirstName("Roger").size());
    }
}
