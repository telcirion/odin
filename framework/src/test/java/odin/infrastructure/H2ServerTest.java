package odin.infrastructure;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class H2ServerTest {
    @Test
    public void serverTest() {
        var sut = new H2Server();
        sut.startServer();
        assertTrue(sut.isRunning());
        sut.stopServer();
        assertFalse(sut.isRunning());
    }
}