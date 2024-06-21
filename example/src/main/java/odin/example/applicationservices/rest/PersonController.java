
package odin.example.applicationservices.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import odin.example.readmodel.PersistableReadModelPerson;
import odin.example.readmodel.PersonReadModelService;
import odin.example.readmodel.PersonReadModelUpdater;
import odin.infrastructure.EventStore;

@RestController
public class PersonController {

    @Autowired
    PersonReadModelService personService;
    @Autowired
    EventStore eventStore;
    @Autowired
    PersonReadModelUpdater personReadModelUpdater;

    @GetMapping(path = "/person/{id}")
    public PersistableReadModelPerson getPerson(@PathVariable int id) {
        return personService.findPersonById(id);
    }

}
