package com.Springboot.Security_client.event.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import com.Springboot.Security_client.entity.User;
import com.Springboot.Security_client.event.RegistrationCompleteEvent;
import com.Springboot.Security_client.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements 
													ApplicationListener<RegistrationCompleteEvent>{

	@Autowired
	private UserService userService;
	
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		userService.saveVerificationTokenForUser(user,token);
		String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;
		
		log.info("click to verify: {} ",url);
		                                            
	}

}