package com.appsstuff.pharma.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.appsstuff.pharma.security.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);  
	User findByEmailAndEnabled(String email , boolean isEnabled);
}
