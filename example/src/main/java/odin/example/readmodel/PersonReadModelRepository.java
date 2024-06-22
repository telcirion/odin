
package odin.example.readmodel;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface PersonReadModelRepository extends CrudRepository<PersistableReadModelPerson, Long> {

    List<PersistableReadModelPerson> findByFirstName(String firstName);

    List<PersistableReadModelPerson> findByLastName(String lastName);

    PersistableReadModelPerson findById(long id);

    PersistableReadModelPerson findByAggregateRootId(UUID identity);
}
