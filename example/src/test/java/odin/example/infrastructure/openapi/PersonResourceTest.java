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
}