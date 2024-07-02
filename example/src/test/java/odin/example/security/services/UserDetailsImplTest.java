package odin.example.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        // Assuming UserDetailsImpl has a constructor that accepts id, username, and
        // authorities
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        userDetails = new UserDetailsImpl(1L, "testUser", "test@example.org", "password", authorities);
    }

    @Test
    public void testGetId() {
        assertEquals(1L, userDetails.getId(), "The ID should match the one set in setUp");
    }

    @Test
    public void testGetUsername() {
        assertEquals("testUser", userDetails.getUsername(), "The username should match the one set in setUp");
    }

    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")),
                "The authorities should contain 'ROLE_USER'");
    }

    // Assuming default implementations for UserDetails interface compliance
    @Test
    public void testUserDetailsCompliance() {
        assertTrue(userDetails.isAccountNonExpired(), "Account should be non-expired");
        assertTrue(userDetails.isAccountNonLocked(), "Account should be non-locked");
        assertTrue(userDetails.isCredentialsNonExpired(), "Credentials should be non-expired");
        assertTrue(userDetails.isEnabled(), "Account should be enabled");
    }
}
