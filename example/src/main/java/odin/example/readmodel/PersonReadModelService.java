
package odin.example.readmodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonReadModelService {

    private final PersonReadModelRepository repo;

    public PersonReadModelService(@Autowired PersonReadModelRepository repo) {
        this.repo = repo;
    }

    public PersistableReadModelPerson findPersonById(int id) {
        return repo.findById(id);
    }
}
