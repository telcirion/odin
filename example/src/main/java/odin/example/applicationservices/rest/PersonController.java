
package odin.example.applicationservices.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import odin.common.Result;
import odin.domainmodel.DomainEvent;
import odin.example.applicationservices.commandhandlers.PersonCommandHandler;
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

    @PostMapping("/person/register")
    public Result postMethodName(@RequestBody RegisterPerson command) {
        Result r = pc.handle(command);

        return r;
    }

    @GetMapping("/events")
    public List<DomainEvent> getEvents() {
        return es.load();
    }

    @GetMapping("/persons")
    public Iterable<PersistableReadModelPerson> getPersons() {
        return personReadModelUpdater.getReadModelRepository().findAll();
    }

    @GetMapping("/person/{id}")
    public PersistableReadModelPerson getPerson(@PathVariable int id) {
        return personReadModelUpdater.getReadModelRepository().findById(id);
    }

}
