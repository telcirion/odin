package odin.example.infrastructure.openapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class PersonResourceTest {
    @Test
    void testPersonGetEndpoint() {
        given().when().get("/persons").then().statusCode(200).body(is("{\"name\":null,\"ssn\":null}"));
    }

    @Test
    void testPersonPostRegisterPersonEndpoint() {
        given().when().post("/persons/registrations").then().statusCode(415);
    }

    @Test
    void testPersonPostChangePersonNameEndpoint() {
        given().when().post("/persons/namechanges").then().statusCode(415);
    }

}