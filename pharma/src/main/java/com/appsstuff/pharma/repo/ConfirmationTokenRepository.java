package com.appsstuff.pharma.repo;

import org.springframework.data.repository.CrudRepository;

import com.appsstuff.pharma.security.model.ConfirmationToken;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
	ConfirmationToken findByConfirmationToken(String confirmationToken);
}