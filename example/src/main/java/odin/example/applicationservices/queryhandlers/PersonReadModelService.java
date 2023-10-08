package odin.example.applicationservices.queryhandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import odin.example.infrastructure.PersonReadModelRepository;
import odin.example.readmodel.PersistableReadModelPerson;

@Service
public class PersonReadModelService {

    @Autowired
    PersonReadModelRepository repo;

    public PersistableReadModelPerson findPersonById(int id) {
        return repo.findById(id);
    }
}
