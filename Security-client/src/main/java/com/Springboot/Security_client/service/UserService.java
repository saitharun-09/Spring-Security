package com.Springboot.Security_client.service;

import java.util.Optional;

import com.Springboot.Security_client.entity.User;
import com.Springboot.Security_client.entity.VerificationToken;
import com.Springboot.Security_client.model.UserModel;

public interface UserService {

	User registerUser(UserModel userModel);

	void saveVerificationTokenForUser(User user, String token);

	String validateVerificationToken(String token);

	VerificationToken generateNewVerificationToken(String oldToken);

	User findUserByEmail(String email);

	void createUserPasswordResetTokenForUser(User user, String token);

	String validatePasswordResetToken(String token);

	Optional<User> getUserByPasswordResetToken(String token);

	void changePassword(User user, String newPassword);

	boolean checkIfOldPasswordIsValid(User user, String oldPassword);
}
