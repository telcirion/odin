
package odin.example.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import odin.common.Result;
import odin.domainmodel.DomainEvent;
import odin.example.applicationservices.commandhandlers.PersonCommandHandler;
import odin.example.domain.commands.ChangePersonName;
import odin.example.domain.commands.RegisterPerson;
import odin.example.domain.state.Person;
import odin.example.readmodel.PersistableReadModelPerson;
import odin.example.readmodel.PersonReadModelUpdater;
import odin.infrastructure.EventRepository;
import odin.infrastructure.EventStore;
import odin.infrastructure.SimplePubSub;

@RestController
public class PersonController {

    private final PersonReadModelUpdater personReadModelUpdater;
    private final SimplePubSub eventBus;
    private final PersonCommandHandler pc;
    private final EventStore es;

    public PersonController(EventStore eventStore, PersonReadModelUpdater personReadModelUpdater) {
        this.personReadModelUpdater = personReadModelUpdater;
        this.eventBus = new SimplePubSub();
        this.es = eventStore;
        EventRepository<Person> personRepository = new EventRepository<>(eventStore, eventBus);
        pc = new PersonCommandHandler(personRepository);
        eventBus.subscribe(personReadModelUpdater);
    }

    @PostMapping("/person/commands/register")
    @Operation(description = "Post a register person command.")
    public Result postMethodName(@RequestBody RegisterPerson command) {
        Result r = pc.handle(command);
        return r;
    }

    @PostMapping("/person/commands/changename")
    @Operation(description = "Post a change person name command.")
    public Result postMethodName(@RequestBody ChangePersonName command) {
        Result r = pc.handle(command);
        return r;
    }

    @GetMapping("/person/source/{id}")
    public Person source(
            @Parameter(name = "id", description = "id of the aggregate root to be retrieved") @PathVariable UUID id) {
        return new EventRepository<Person>(es, eventBus).load(id, Person::new).getAggregateRoot();
    }

    @GetMapping("/person/events")
    public List<DomainEvent> getEvents() {
        return es.load();
    }

    @GetMapping("/persons/readmodel")
    public Iterable<PersistableReadModelPerson> getPersons() {
        return personReadModelUpdater.getReadModelRepository().findAll();
    }

    @GetMapping("/person/readmodel/{id}")
    public PersistableReadModelPerson getPerson(
            @Parameter(name = "id", description = "id of the aggregate root to be retrieved") @PathVariable int id) {
        return personReadModelUpdater.getReadModelRepository().findById(id);
    }

}
