package com.template.entities.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.template.entities.user.Role;
import com.template.entities.user.CustomUser;

public interface UserRepository extends JpaRepository<CustomUser, UUID> {
	
	Optional<CustomUser> findByEmail(String email);

	@Query("SELECT u FROM CustomUser u WHERE " + "(u.role = :role) ")
	Page<CustomUser> findAllUsers(Pageable pagina, @Param("role") Role ruolo);
}
