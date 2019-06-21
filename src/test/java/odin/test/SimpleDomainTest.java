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
import odin.test.applicationservices.commands.ChangePersonName;
import odin.test.applicationservices.denormalizers.PersonDenormalizer;
import odin.test.applicationservices.processmanagers.SignUpPersonProcessManager;
import odin.test.applicationservices.queries.PersonByNameQuery;
import odin.test.applicationservices.queryhandlers.PersonQueryHandler;
import odin.test.applicationservices.queryresults.PersonQueryResult;
import odin.test.domain.events.PersonSignUpReceived;
import odin.test.domain.state.Person;

class SimpleDomainTest {

    private static final String EVENT_QUEUE ="activemq:eventQueue?jmsMessageType=Object";
    private static final String COMMAND_QUEUE ="activemq:commandQueue?jmsMessageType=Object";

    @Test
    void test() {
        final Logger logger=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

        H2DBServer databaseServer=new H2DBServer();
        databaseServer.startServer();

        SQLEventStore<Person> personRepository=new SQLEventStore<>(new TestDataSource());
        personRepository.createDatabase(); // it's only signUpPersonProcessManager test

        ISendMessage eventBus=new SimpleMessageBus(EVENT_QUEUE);
        ISendMessage commandBus=new SimpleMessageBus(COMMAND_QUEUE);

        //start processManager
        var signUpPersonProcessManager=new SignUpPersonProcessManager(commandBus);
        //var personDenormalizer=new PersonDenormalizer();


        ((IConsumeMessage)eventBus).consume(signUpPersonProcessManager);
        logger.info("ProcessManager created, wait for processing.");
        //TODO make the denormalizer also consume the events
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
        //PersonQueryHandler queryHandler=new PersonQueryHandler(personDenormalizer.getReadModel());
        //PersonQueryResult personQueryResult=queryHandler.query(new PersonByNameQuery("John"));
        //if(personQueryResult !=null){
        //   logger.info("Person found with name: "+personQueryResult.getPerson().getName() +
        //            " and ssn: " + personQueryResult.getPerson().getSsn());
        
            //and then change the person's name
         //   commandBus.send(new ChangePersonName("Nico", personQueryResult.getPerson().getId(), null));
       // }

        //wait for name to be changed.
        //TODO wait for name to be changed
        
        ((IConsumeMessage)eventBus).stop();
        ((IConsumeMessage)commandBus).stop();
        assertTrue(true);
        databaseServer.stopServer();
    }
}
