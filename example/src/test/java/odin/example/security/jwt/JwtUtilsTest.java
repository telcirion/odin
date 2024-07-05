package odin.example.security.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import odin.example.security.services.UserDetailsImpl;

@SpringBootTest
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        // Initialize JwtUtils with test secret and expiration
        jwtUtils = new JwtUtils("testSecretKeyWhichIsNotSoSecretButMustBeLongEnough", 3600000); // 1 hour for testing
    }

    @Test
    void testGenerateJwtToken() {
        // Mock the authentication object
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test", "test@example.org", "test", null);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Generate token
        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token, "The generated token should not be null");
    }

    @Test
    void testValidateJwtToken() {
        // Mock the authentication object
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test", "test@example.org", "test", null);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Generate token
        String token = jwtUtils.generateJwtToken(authentication);

        // Validate token
        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid, "The token should be valid");
    }
}