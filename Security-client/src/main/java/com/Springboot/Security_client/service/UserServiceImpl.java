package com.Springboot.Security_client.service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Springboot.Security_client.entity.PasswordResetToken;
import com.Springboot.Security_client.entity.User;
import com.Springboot.Security_client.entity.VerificationToken;
import com.Springboot.Security_client.model.UserModel;
import com.Springboot.Security_client.repository.PasswordResetTokenRepo;
import com.Springboot.Security_client.repository.UserRepo;
import com.Springboot.Security_client.repository.VerificationTokenRepo;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private VerificationTokenRepo verificationTokenRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PasswordResetTokenRepo passwordResetTokenRepo;
	
	@Override
	public User registerUser(UserModel userModel) {
		User user = new User();
		user.setFirstName(userModel.getFirstName());
		user.setLastName(userModel.getLastName());
		user.setEmail(userModel.getEmail());
		user.setPassword(passwordEncoder.encode(userModel.getPassword()));
		user.setRole("USER");
		
		userRepo.save(user);
		return user;
	}

	@Override
	public void saveVerificationTokenForUser(User user, String token) {
		VerificationToken verificationToken = new VerificationToken(user, token);
		
		verificationTokenRepo.save(verificationToken);
	}

	@Override
	public String validateVerificationToken(String token) {
		VerificationToken verificationToken = verificationTokenRepo.findByToken(token);
		if (verificationToken == null) {
			return "Invailid";
		}
		
		User user= verificationToken.getUser();
		Calendar calendar = Calendar.getInstance();
		
		if (verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
			 verificationTokenRepo.delete(verificationToken);
			 return "Expired";
		}
		
		user.setEnabled(true);
		userRepo.save(user);
		return "Valid";
	}

	@Override
	public VerificationToken generateNewVerificationToken(String oldToken) {
		VerificationToken verificationToken = verificationTokenRepo.findByToken(oldToken);
		verificationToken.setToken(UUID.randomUUID().toString());
		verificationTokenRepo.save(verificationToken);
		return verificationToken;
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public void createUserPasswordResetTokenForUser(User user, String token) {
		PasswordResetToken passwordResetToken =new PasswordResetToken(user, token);
		passwordResetTokenRepo.save(passwordResetToken);
	}

	@Override
	public String validatePasswordResetToken(String token) {
		PasswordResetToken passwordResetToken = passwordResetTokenRepo.findByToken(token);
		if (passwordResetToken == null) {
			return "Invalid";
		}
		
		User user= passwordResetToken.getUser();
		Calendar calendar = Calendar.getInstance();
		
		if (passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
			passwordResetTokenRepo.delete(passwordResetToken);
			 return "Expired";
		}

		return "Valid";
	}

	@Override
	public Optional<User> getUserByPasswordResetToken(String token) {
		return Optional.ofNullable(passwordResetTokenRepo.findByToken(token).getUser());
	}

	@Override
	public void changePassword(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepo.save(user);
	}

	@Override
	public boolean checkIfOldPasswordIsValid(User user, String oldPassword) {
		return passwordEncoder.matches(oldPassword, user.getPassword());
	}

}