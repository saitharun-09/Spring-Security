package com.Springboot.Security_client.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.Springboot.Security_client.entity.User;
import com.Springboot.Security_client.entity.VerificationToken;
import com.Springboot.Security_client.event.RegistrationCompleteEvent;
import com.Springboot.Security_client.model.PasswordModel;
import com.Springboot.Security_client.model.UserModel;
import com.Springboot.Security_client.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RegistrationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@PostMapping("/register")
	public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
		User user = userService.registerUser(userModel);
		publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
		return "Success";
		
	}
	
	@GetMapping("/verifyRegistration")
	public String verifyRegistration(@RequestParam("token") String token) {
		String result = userService.validateVerificationToken(token);
		if (result.equalsIgnoreCase("valid")) {
			return "User successfully verified";
		}
	return "Bad User";
	}
	
	@GetMapping("/resendVerify")
	public String resendVerificationToken(@RequestParam("token") String oldToken,
																	HttpServletRequest request ) {
		VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
		User user = verificationToken.getUser();
		resendVerificationMail(user,applicationUrl(request), verificationToken);
		return "Verification Email sent";	
	}
	
	@PostMapping("/resetPassword")
	public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
		User user = userService.findUserByEmail(passwordModel.getEmail());
		String url = "";
		if (user != null) {
			String token = UUID.randomUUID().toString();
			userService.createUserPasswordResetTokenForUser(user, token);
			url = passwordResetTokenMail(user, applicationUrl(request),token);
		}
		return url;
	}
	
	@PostMapping("/savePassword")
	public String savePassword(@RequestParam("token") String token, 
													@RequestBody PasswordModel passwordModel) {
		String result = userService.validatePasswordResetToken(token);
		if (!result.equalsIgnoreCase("valid")) {
			return "Invalid Token";
		}
		Optional<User> user = userService.getUserByPasswordResetToken(token);
		if (user.isPresent()) {
			userService.changePassword(user.get(),passwordModel.getNewPassword());
			return "Password Reset Successfully";
		}else {
		return "Invalid";
		}
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestBody PasswordModel passwordModel) {
		User user = userService.findUserByEmail(passwordModel.getEmail());
		if (!userService.checkIfOldPasswordIsValid(user, passwordModel.getOldPassword())){
			return "Wrong  Old Password";
		}
		userService.changePassword(user,passwordModel.getNewPassword());
		return "Password changed Successfully";
	}

	private String passwordResetTokenMail(User user, String applicationUrl, String token) {
		String url = applicationUrl +"/savePassword?token=" + token;
		log.info("click to Reset Passowrd: {} ",url);
		return url;
	}

	private void resendVerificationMail(User user, String applicationUrl, 
															VerificationToken verificationToken) {
		String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();
		
		log.info("click to verify: {} ",url);
	}

	@GetMapping("/api/hello")
	public String hello() {
		return "Hello";
	}
	
	private String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort()
																	+ request.getContextPath();
	}

}