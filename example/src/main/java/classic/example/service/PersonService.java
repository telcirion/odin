package classic.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import classic.example.domain.Person;
import classic.example.repositories.PersonRepository;

@Service
public class PersonService {

    @Autowired
    PersonRepository repo;

    public Person findPersonById(int id) {
        return repo.findById(id);
    }
}
