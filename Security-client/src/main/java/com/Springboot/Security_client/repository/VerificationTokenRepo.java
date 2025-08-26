package com.Springboot.Security_client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Springboot.Security_client.entity.VerificationToken;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long>{

	VerificationToken findByToken(String token);

}
