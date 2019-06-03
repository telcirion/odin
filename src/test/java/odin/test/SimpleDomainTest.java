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
package odin.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.invoke.MethodHandles;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.concepts.applicationservices.IConsumeMessage;
import odin.concepts.applicationservices.ISendMessage;
import odin.infrastructure.H2DBServer;
import odin.infrastructure.SQLEventStore;
import odin.infrastructure.SimpleMessageBus;
import odin.test.applicationservices.commandhandlers.PersonCommandHandler;
import odin.test.applicationservices.processmanagers.SignUpPersonProcessManager;
import odin.test.applicationservices.queries.PersonByNameQuery;
import odin.test.applicationservices.queryhandlers.PersonQueryHandler;
import odin.test.applicationservices.queryresults.PersonQueryResult;
import odin.test.domain.events.PersonSignUpReceived;
import odin.test.domain.state.Person;

class SimpleDomainTest {

    private static final String EVENT_QUEUE ="vm:eventQueue";
    private static final String COMMAND_QUEUE ="vm:commandQueue";

    @Test
    void test() {
        final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

        H2DBServer.startServer();
        if(H2DBServer.isRunning()){
            logger.info("Database seems to be running.");
        } else {
            logger.info("Database seems not to be running.");
        }
        SQLEventStore<Person> personRepository=new SQLEventStore<>(new TestDataSource());
        personRepository.createDatabase(); // it's only signUpPersonProcessManager test

        ISendMessage eventBus=new SimpleMessageBus(EVENT_QUEUE);
        ISendMessage commandBus=new SimpleMessageBus(COMMAND_QUEUE);

        //start processManager
        var signUpPersonProcessManager=new SignUpPersonProcessManager(commandBus);
        ((IConsumeMessage)eventBus).consume(signUpPersonProcessManager);
        logger.info("ProcessManager created, wait for processing.");

        //start commandHandler
        ((IConsumeMessage)commandBus).consume(new PersonCommandHandler(eventBus, personRepository));
        logger.info("CommandHandler created, wait for processing.");

        //send first event
        eventBus.send(new PersonSignUpReceived("1234567892","Peter"));
        eventBus.send(new PersonSignUpReceived("1234567893","John"));

        logger.info("DomainEvent send.");

        //after processing 2 registrations, we're done.
        //noinspection StatementWithEmptyBody
        while (signUpPersonProcessManager.getNumberOfPersonRegisteredReceived()<2);
        logger.info("All DomainEvents were processed.");

        //let's try signUpPersonProcessManager query
        PersonQueryHandler queryHandler=new PersonQueryHandler();
        PersonQueryResult personQueryResult=queryHandler.query(new PersonByNameQuery("no name"));
        if(personQueryResult !=null){
            logger.info("Person found with name: "+personQueryResult.getPerson().getName() +
                    " and ssn: " + personQueryResult.getPerson().getSsn());
        }
        assertTrue(true);
        H2DBServer.stopServer();
    }
}
