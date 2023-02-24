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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.demo.authappservice.entity.User;
import com.demo.authappservice.jwt.JwtTokenProvider;
import com.demo.authappservice.service.UserService;
import com.demo.authappservice.util.AppUtil;

@CrossOrigin("*")
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping(value = "/api/dev/loaduser")
	public User loadUserDetails(@RequestHeader HttpHeaders headers) {
		return userService.loadUserDetails(AppUtil.getLoggedUserFromHeader(headers));
	}

	@PostMapping(value = "/api/user/authenticate")
	public User authenticate(@RequestBody User user) {
		User userAuth = new User();
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			if (authentication.isAuthenticated()) {
				userAuth = (User) authentication.getPrincipal();
				userAuth.setPassword("");
				userAuth.setMessage(jwtTokenProvider.generateToken(userAuth.getUsername()));
				logger.info("User {} login as {}", userAuth.getUsername(), userAuth.getRole());
			}
		} catch (Exception e) {
			logger.error("Authentication error for " + user.getUsername() + " : Exception - " + e.getMessage());
		}
		return userAuth;
	}

	@GetMapping(value = "/api/user/refreshjwttoken")
	public String refreshJWTToken(@RequestHeader HttpHeaders headers, String username, String forceRefresh) {
		return jwtTokenProvider.refreshJWTToken(username, AppUtil.getTokenFromHeader(headers), forceRefresh);
	}

	@GetMapping(value = "/api/user/otp")
	public int getOTP(String username) {
		return userService.getOTP(username);
	}

	@PostMapping(value = "/api/user/adduser")
	public String addNewUser(@RequestHeader HttpHeaders headers, @RequestBody User newUser) {
		return userService.addNewUser(newUser);
	}

	@PostMapping(value = "/api/user/resetpassword")
	public String resetPassword(@RequestBody User resetUser) {
		return userService.resetPassword(resetUser);
	}

	@PutMapping(value = "/api/manage/user/save")
	public String saveUser(@RequestHeader HttpHeaders headers, @RequestBody User user) {
		return userService.saveUser(user, AppUtil.getLoggedUserFromHeader(headers));
	}
	
	@DeleteMapping(value = "/api/manage/user/delete")
	public String deleteUser(@RequestHeader HttpHeaders headers, @RequestBody User user) {
		return userService.deleteUser(user, AppUtil.getLoggedUserFromHeader(headers));
	}

}
