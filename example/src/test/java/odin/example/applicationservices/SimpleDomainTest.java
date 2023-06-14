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

import java.lang.invoke.MethodHandles;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.example.applicationservices.commandhandlers.PersonCommandHandler;
import odin.example.applicationservices.denormalizers.PersonDeNormalizer;
import odin.example.applicationservices.processmanagers.SignUpPersonProcessManager;
import odin.example.applicationservices.queries.PersonByNameQuery;
import odin.example.applicationservices.queryhandlers.PersonQueryHandler;
import odin.example.applicationservices.queryresults.PersonQueryResult;
import odin.example.domain.commands.ChangePersonName;
import odin.example.domain.events.PersonSignUpReceived;
import odin.example.domain.state.Person;
import odin.framework.applicationservices.EventRepository;
import odin.framework.infrastructure.InMemoryEventStore;
import odin.framework.infrastructure.SimplePubSub;

class SimpleDomainTest {

    @Test
    void test() {
        final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

        final SimplePubSub eventBus = new SimplePubSub();
        final SimplePubSub commandBus = new SimplePubSub();

        // start processManager
        SignUpPersonProcessManager signUpPersonProcessManager = new SignUpPersonProcessManager(commandBus);
        eventBus.subscribe(signUpPersonProcessManager);
        logger.info("ProcessManager created, wait for processing.");

        // start de-normalizer
        PersonDeNormalizer personDeNormalizer = new PersonDeNormalizer();
        eventBus.subscribe(personDeNormalizer);
        logger.info("De-normalizer created, wait for processing.");

        // Initialize repo & storage
        // final SqlEventStore eventStore = new SqlEventStore(new TestDataSource());
        // eventStore.createDatabase(); // it's only signUpPersonProcessManager test
        InMemoryEventStore eventStore = new InMemoryEventStore();
        EventRepository<Person> personRepository = new EventRepository<>(eventStore, eventBus);

        // start commandHandler
        commandBus.subscribe(new PersonCommandHandler(personRepository));
        logger.info("CommandHandler created, wait for processing.");

        // send first event
        eventBus.send(new PersonSignUpReceived("1234567892", "Peter"));
        eventBus.send(new PersonSignUpReceived("1234567893", "John"));

        logger.info("DomainEvents send.");

        // after processing 2 registrations, we're done.
        // noinspection StatementWithEmptyBody
        while (personDeNormalizer.getNumberOfPersonRegisteredReceived() < 2) {
            // do nothing, just wait.
        }
        logger.info("All DomainEvents (PersonRegistered) were processed by the de-normalizer.");

        // let's try a person query
        PersonQueryHandler queryHandler = new PersonQueryHandler(personDeNormalizer.getReadModel());
        PersonQueryResult personQueryResult = queryHandler.query(new PersonByNameQuery("Peter"));
        if (personQueryResult != null) {
            logger.info("Person found with name: " + personQueryResult.person().name() + " and ssn: "
                    + personQueryResult.person().ssn());

            // and then change the person's name
            commandBus.send(new ChangePersonName("Nico", personQueryResult.person().id(), null));
        }

        // wait for name to be changed.
        // noinspection StatementWithEmptyBody
        while (personDeNormalizer.getNumberOfPersonNameChangedReceived() < 1) {
            // do nothing, just wait.
        }
        logger.info("All DomainEvents (PersonNameChanged) were processed by the de-normalizer.");

        // and check if the name is changed
        PersonQueryResult anotherPersonQueryResult = queryHandler.query(new PersonByNameQuery("Nico"));
        if (anotherPersonQueryResult != null) {
            logger.info("Person found with name: " + anotherPersonQueryResult.person().name() + " and ssn: "
                    + anotherPersonQueryResult.person().ssn());
        }

        eventBus.stop();
        commandBus.stop();

        assertEquals("Nico", anotherPersonQueryResult.person().name());
        // Peter should no longer be found
        assertEquals(null, queryHandler.query(new PersonByNameQuery("Peter")).person());
    }
}
