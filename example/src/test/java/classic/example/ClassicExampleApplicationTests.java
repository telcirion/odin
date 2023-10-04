package classic.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import classic.example.domain.Person;
import classic.example.repositories.PersonRepository;

@SpringBootTest
class ClassicExampleApplicationTests {
    @Autowired
    PersonRepository repo;

    @Test
    void contextLoads() {
    }

    @Test
    void personRepositoryTest() {

        Person person = new Person(null, "Peter", "Jansen");
        repo.save(person);

        var otherPerson = repo.findByFirstName("Peter").get(0);

        otherPerson.setFirstName("Pieter");
        repo.save(otherPerson);

        var anOtherPerson = repo.findByLastName("Jansen");
        assertEquals(anOtherPerson.get(0).getFirstName(), "Pieter");
    }

}
