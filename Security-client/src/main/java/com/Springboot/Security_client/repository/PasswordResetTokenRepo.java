package com.Springboot.Security_client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Springboot.Security_client.entity.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long> {

	PasswordResetToken findByToken(String token);

}