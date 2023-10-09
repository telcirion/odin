package odin.example.readmodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonReadModelService {

    @Autowired
    PersonReadModelRepository repo;

    public PersistableReadModelPerson findPersonById(int id) {
        return repo.findById(id);
    }
}
