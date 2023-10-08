package odin.example.applicationservices.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import odin.example.applicationservices.queryhandlers.PersonReadModelService;
import odin.example.readmodel.PersistableReadModelPerson;

@RestController
public class PersonController {

    @Autowired
    PersonReadModelService personService;

    @GetMapping(path = "/person/{id}")
    public PersistableReadModelPerson getBook(@PathVariable int id) {
        return personService.findPersonById(id);
    }

}