
package odin.example.applicationservices.commandhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import odin.applicationservices.CommandHandler;
import odin.applicationservices.Repository;
import odin.common.Identity;
import odin.common.Message;
import odin.common.MessageDispatcher;
import odin.common.MessageHandler;
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

    private CommandHandler handle(RegisterPerson registerPerson) {
        this.log(registerPerson);
        var p = new Aggregate<>(new Identity(), new Person());
        p.process(registerPerson);
        personRepository.save(p);
        LOGGER.info("Person with first name {} and last name {} registered.", p.getAggregateRoot().getFirstName(),
                p.getAggregateRoot().getLastName());
        return this;
    }

    private CommandHandler handle(ChangePersonName changePersonName) {
        this.log(changePersonName);
        var p = personRepository.load(changePersonName.getMessageInfo().subjectId(), Person::new);
        p.process(changePersonName);
        personRepository.save(p);
        return this;
    }

    @Override
    public MessageHandler handle(Message msg) {
        return new MessageDispatcher<CommandHandler>(this).match(RegisterPerson.class, this::handle, msg)
                .match(ChangePersonName.class, this::handle, msg).result();
    }

    private void log(Command command) {
        LOGGER.info("Command {} received for aggregateId: {}, revision: {}", command.getClass().getSimpleName(),
                command.getMessageInfo().subjectId(), command.getMessageInfo().subjectVersion());
    }
}
