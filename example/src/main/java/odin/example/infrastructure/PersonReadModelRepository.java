package odin.example.infrastructure;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import odin.example.readmodel.PersistableReadModelPerson;

public interface PersonReadModelRepository extends CrudRepository<PersistableReadModelPerson, Long> {

    List<PersistableReadModelPerson> findByFirstName(String firstName);

    List<PersistableReadModelPerson> findByLastName(String lastName);

    PersistableReadModelPerson findById(long id);
}