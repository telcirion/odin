package odin.example.infrastructure.openapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import odin.example.domain.commands.ChangePersonName;
import odin.example.domain.commands.RegisterPerson;

@QuarkusTest
class PersonResourceTest {
    @Test
    void testPersonGetEndpoint() {
        given().when().get("/persons").then().statusCode(200).body(is("{\"name\":null,\"ssn\":null}"));
    }

    @Test
    void testPersonPostRegisterPersonEndpoint() {
        given().contentType(MediaType.APPLICATION_JSON).body(new RegisterPerson(null, "789", "Bob")).when()
                .post("/persons/registrations/").then().statusCode(202);
    }

    @Test
    void testPersonPostChangePersonNameEndpoint() {
        given().contentType(MediaType.APPLICATION_JSON).body(new ChangePersonName("Alice", null, null)).when()
                .post("/persons/namechanges").then().statusCode(202);
    }

}