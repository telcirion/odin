package odin.example.security.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import odin.example.security.jwt.JwtUtils;
import odin.example.security.model.Role;
import odin.example.security.model.RoleType;
import odin.example.security.payload.request.LoginRequest;
import odin.example.security.payload.request.SignupRequest;
import odin.example.security.payload.response.JwtResponse;
import odin.example.security.payload.response.MessageResponse;
import odin.example.security.repositories.RoleRepository;
import odin.example.security.repositories.UserRepository;
import odin.example.security.services.UserDetailsImpl;

public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("null")
    @Test
    void testLogin() {
        // Setup
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtUtils.generateJwtToken(auth)).thenReturn("dummy-token");
        HashSet<Role> roles = new HashSet<>();
        roles.add(new Role(RoleType.ROLE_ADMIN));
        roles.add(new Role(RoleType.ROLE_MODERATOR));
        roles.add(new Role(RoleType.ROLE_USER));

        when(auth.getPrincipal()).thenReturn(new UserDetailsImpl(1L, "user", "password", "email", roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .toList()));
        LoginRequest loginRequest = new LoginRequest("user", "password");
        // Execute
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof JwtResponse);
        assertEquals("dummy-token", ((JwtResponse) response.getBody()).getToken());
    }

    @SuppressWarnings("null")
    @Test
    void testSignup() {
        // Setup
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("newUser@example.com");
        signupRequest.setPassword("password");
        HashSet<String> roles = new HashSet<>();
        roles.add("admin");
        roles.add("user");
        roles.add("mod");
        signupRequest.setRole(roles);

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");
        // Assume roleRepository and other necessary setup here
        when(roleRepository.findByName(RoleType.ROLE_ADMIN)).thenReturn(Optional.of(new Role(RoleType.ROLE_ADMIN)));
        when(roleRepository.findByName(RoleType.ROLE_MODERATOR))
                .thenReturn(Optional.of(new Role(RoleType.ROLE_MODERATOR)));
        when(roleRepository.findByName(RoleType.ROLE_USER)).thenReturn(Optional.of(new Role(RoleType.ROLE_USER)));
        // Execute
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
    }
}