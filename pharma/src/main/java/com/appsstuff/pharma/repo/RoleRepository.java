package com.appsstuff.pharma.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appsstuff.pharma.security.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
