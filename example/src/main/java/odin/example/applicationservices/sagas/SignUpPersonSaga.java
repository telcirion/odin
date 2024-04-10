
package odin.example.applicationservices.sagas;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.applicationservices.SagaManager;
import odin.common.Identity;
import odin.common.Message;
import odin.common.MessageDispatcher;
import odin.common.Result;
import odin.common.SendMessage;
import odin.example.domain.commands.RegisterPerson;
import odin.example.domain.events.PersonRegistered;
import odin.example.domain.events.PersonSignUpReceived;

public class SignUpPersonSaga implements SagaManager {

    private final SendMessage commandBus;

    final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SignUpPersonSaga(SendMessage commandBus) {
        this.commandBus = commandBus;
    }

    @Override
    public Result handle(Message msg) {
        return new MessageDispatcher<>(Result.NOK).match(PersonSignUpReceived.class, this::handle, msg)
                .match(PersonRegistered.class, this::handle, msg).result();

    }

    private Result handle(PersonSignUpReceived msg) {
        logReception(msg);
        commandBus.send(new RegisterPerson(new Identity(), msg.getLastName(), msg.getFirstName()));
        return Result.OK;
    }

    private Result handle(PersonRegistered msg) {
        logReception(msg);
        return Result.OK;
    }

    private void logReception(Message msg) {
        logger.info("Event {} received, on {}.", msg.getClass().getSimpleName(), msg.getMessageInfo().timestamp());
    }
}
