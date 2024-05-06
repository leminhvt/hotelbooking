package com.hotelbooking.repository;

import java.util.Optional;

import com.hotelbooking.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(String roleUser);

	boolean existsByName(String theRole);

}
