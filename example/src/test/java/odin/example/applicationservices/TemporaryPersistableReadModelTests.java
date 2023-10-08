package odin.example.applicationservices;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import odin.example.infrastructure.PersonReadModelRepository;
import odin.example.readmodel.PersistableReadModelPerson;

@SpringBootTest
class TemporaryPersistableReadModelTests {
    @Autowired
    PersonReadModelRepository repo;

    @Test
    void contextLoads() {
    }

    @Test
    void personRepositoryTest() {

        PersistableReadModelPerson person = new PersistableReadModelPerson(null, "Peter", "Jansen");
        repo.save(person);

        var otherPerson = repo.findByFirstName("Peter").get(0);

        otherPerson.setFirstName("Pieter");
        repo.save(otherPerson);

        var anOtherPerson = repo.findByLastName("Jansen");
        assertEquals(anOtherPerson.get(0).getFirstName(), "Pieter");
    }

}
