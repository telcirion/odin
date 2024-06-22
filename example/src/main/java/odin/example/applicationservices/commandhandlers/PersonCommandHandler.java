
package odin.example.applicationservices.commandhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.applicationservices.CommandHandler;
import odin.applicationservices.Repository;
import odin.common.Message;
import odin.common.MessageDispatcher;
import odin.common.Result;
import odin.domainmodel.Aggregate;
import odin.domainmodel.Command;
import odin.example.domain.commands.ChangePersonName;
import odin.example.domain.commands.RegisterPerson;
import odin.example.domain.state.Person;

public class PersonCommandHandler implements CommandHandler {

    private final Repository<Person> personRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonCommandHandler.class);

    public PersonCommandHandler(Repository<Person> personRepository) {
        this.personRepository = personRepository;
    }

    private Result handle(RegisterPerson registerPerson) {
        this.log(registerPerson);
        var p = new Aggregate<>(new Person());
        p.process(registerPerson);
        personRepository.save(p);
        return Result.OK;
    }

    private Result handle(ChangePersonName changePersonName) {
        this.log(changePersonName);
        var p = personRepository.load(changePersonName.getAggregateRootId(), Person::new);
        p.process(changePersonName);
        personRepository.save(p);
        return Result.OK;
    }

    @Override
    public Result handle(Message msg) {
        return new MessageDispatcher<>(Result.NOK).match(RegisterPerson.class, this::handle, msg)
                .match(ChangePersonName.class, this::handle, msg).result();

    }

    private void log(Command command) {
        LOGGER.info("Command {} received for aggregateId: {}, revision: {}", command.getClass().getSimpleName(),
                command.getAggregateRootId(), command.getAggregateVersion());
    }
}
