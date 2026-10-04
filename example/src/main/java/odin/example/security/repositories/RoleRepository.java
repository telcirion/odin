package odin.example.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import odin.example.security.model.Role;
import odin.example.security.model.RoleType;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
