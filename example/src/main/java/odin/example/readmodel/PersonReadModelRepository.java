package odin.example.readmodel;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import odin.common.Identity;

public interface PersonReadModelRepository extends CrudRepository<PersistableReadModelPerson, Long> {

    List<PersistableReadModelPerson> findByFirstName(String firstName);

    List<PersistableReadModelPerson> findByLastName(String lastName);

    PersistableReadModelPerson findById(long id);

    PersistableReadModelPerson findByIdentity(Identity identity);
}