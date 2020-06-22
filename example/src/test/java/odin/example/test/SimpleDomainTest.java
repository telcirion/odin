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

package odin.example.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.invoke.MethodHandles;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.example.applicationservices.commandhandlers.PersonCommandHandler;
import odin.example.applicationservices.commands.ChangePersonName;
import odin.example.applicationservices.denormalizers.PersonDenormalizer;
import odin.example.applicationservices.processmanagers.SignUpPersonProcessManager;
import odin.example.applicationservices.queries.PersonByNameQuery;
import odin.example.applicationservices.queryhandlers.PersonQueryHandler;
import odin.example.applicationservices.queryresults.PersonQueryResult;
import odin.example.domain.events.PersonSignUpReceived;
import odin.example.domain.state.Person;
import odin.infrastructure.SimpleMessageBus;
import odin.infrastructure.SqlEventRepository;

class SimpleDomainTest {

    @Test
    void test() {
        final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

        final SimpleMessageBus eventBus = new SimpleMessageBus(SimpleMessageBus.BusType.TOPIC);
        final SimpleMessageBus commandBus = new SimpleMessageBus(SimpleMessageBus.BusType.QUEUE);

        SqlEventRepository<Person> personRepository = new SqlEventRepository<>(new TestDataSource(), eventBus);
        personRepository.createDatabase(); // it's only signUpPersonProcessManager test

        // start processManager
        SignUpPersonProcessManager signUpPersonProcessManager = new SignUpPersonProcessManager(commandBus);
        eventBus.consume(signUpPersonProcessManager);
        logger.info("ProcessManager created, wait for processing.");

        // start denormalizer
        PersonDenormalizer personDenormalizer = new PersonDenormalizer();
        eventBus.consume(personDenormalizer);
        logger.info("Denormalizer created, wait for processing.");

        // start commandHandler
        commandBus.consume(new PersonCommandHandler(personRepository));
        logger.info("CommandHandler created, wait for processing.");

        // send first event
        eventBus.send(new PersonSignUpReceived("1234567892", "Peter"));
        eventBus.send(new PersonSignUpReceived("1234567893", "John"));

        logger.info("DomainEvents send.");

        // after processing 2 registrations, we're done.
        // noinspection StatementWithEmptyBody
        while (personDenormalizer.getNumberOfPersonRegisteredReceived() < 2) {
            // do nothing, just wait.
        }
        logger.info("All DomainEvents (PersonRegistered) were processed by the denormalizer.");

        // let's try a person query
        PersonQueryHandler queryHandler = new PersonQueryHandler(personDenormalizer.getReadModel());
        PersonQueryResult personQueryResult = queryHandler.query(new PersonByNameQuery("Peter"));
        if (personQueryResult != null) {
            logger.info("Person found with name: " + personQueryResult.getPerson().getName() + " and ssn: "
                    + personQueryResult.getPerson().getSsn());

            // and then change the person's name
            commandBus.send(new ChangePersonName("Nico", personQueryResult.getPerson().getId(), null));
        }

        // wait for name to be changed.
        // noinspection StatementWithEmptyBody
        while (personDenormalizer.getNumberOfPersonNameChangedReceived() < 1) {
            // do nothing, just wait.
        }
        logger.info("All DomainEvents (PersonNameChanged) were processed by the denormalizer.");

        // and check if the name is changed
        PersonQueryResult anotherPersonQueryResult = queryHandler.query(new PersonByNameQuery("Nico"));
        if (anotherPersonQueryResult != null) {
            logger.info("Person found with name: " + anotherPersonQueryResult.getPerson().getName() + " and ssn: "
                    + anotherPersonQueryResult.getPerson().getSsn());
        }

        eventBus.stop();
        commandBus.stop();
        assertTrue(anotherPersonQueryResult.getPerson().getName().equals("Nico"));
    }
}
