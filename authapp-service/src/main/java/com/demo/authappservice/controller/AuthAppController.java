package com.demo.authappservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.demo.authappservice.entity.User;
import com.demo.authappservice.jwt.JwtTokenProvider;
import com.demo.authappservice.service.AuthAppService;
import com.demo.authappservice.util.AppUtil;

@CrossOrigin("*")
@RestController
public class AuthAppController {
	
	@Autowired
	private AuthAppService appService;	

	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;	
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/api/admin/loadallusers")
	public List<User> retrieveAllUsers() {
		return appService.retrieveAllUsers();
	}
	
	@GetMapping(value = "/api/dev/loaduser")
	public List<User> loadUserDetails(@RequestHeader HttpHeaders headers) {
		return appService.loadUserDetails(AppUtil.getLoggedUserFromHeader(headers));
	}	
	
	@PostMapping(value = "/api/user/authenticate")
	public List<User> authenticate(@RequestBody User user) {
		List<User> userList = new ArrayList<User>();
		try {
		Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			if (authentication.isAuthenticated()) {
				User userAuth = (User) authentication.getPrincipal();
				userAuth.setPassword("");
				userAuth.setMessage(jwtTokenProvider.createToken(userAuth.getUsername(), userAuth.getRole()));
				userList.add(userAuth);
				logger.info("User {} login as {}", userAuth.getUsername(), userAuth.getRole());
			}
		} catch (Exception e) {
			logger.error("Authentication error for " + user.getUsername() + " : Exception - " + e.getMessage());
		}
		return userList;
	}
	/*
	@GetMapping(value = "/api/user/refreshjwttoken/{username}/{userrole}")
	public List<String> refreshJWTToken(@RequestHeader HttpHeaders headers, @PathVariable String username,
			@PathVariable String userrole) {
		return tokenProvider.refreshJWTToken(username, userrole, AppUtil.getTokenFromHeader(headers));
	}

	
	
	@GetMapping(value = "/api/user/otp/{username}")
	public int getOTP(@PathVariable String username) {
		return appService.getOTP(username);
	}

	@PostMapping(value = "/api/manage/user/resetpassword")
	public int resetPassword(@RequestBody User resetUser) {
		return appService.resetPassword(resetUser);
	}

	@PostMapping(value = "/api/user/adduser")
	public int addNewUser(@RequestHeader HttpHeaders headers, @RequestBody User newUser) {
		return appService.addNewUser(newUser);
	}

	@PutMapping(value = "/api/user/save")
	public int saveUser(@RequestHeader HttpHeaders headers, @RequestBody User user) {
		return appService.saveUser(user, AppUtil.getLoggedUserFromHeader(headers));
	}
*/
}
