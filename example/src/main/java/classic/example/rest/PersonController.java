package classic.example.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import classic.example.domain.Person;
import classic.example.service.PersonService;

@RestController
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping(path = "/person/{id}")
    public Person getBook(@PathVariable int id) {
        return personService.findPersonById(id);
    }

}