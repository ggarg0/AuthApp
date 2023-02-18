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
	public User loadUserDetails(@RequestHeader HttpHeaders headers) {
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
				userAuth.setMessage(jwtTokenProvider.generateToken(userAuth.getUsername()));
				userList.add(userAuth);
				logger.info("User {} login as {}", userAuth.getUsername(), userAuth.getRole());
			}
		} catch (Exception e) {
			logger.error("Authentication error for " + user.getUsername() + " : Exception - " + e.getMessage());
		}
		return userList;
	}

	@GetMapping(value = "/api/user/refreshjwttoken")
	public String refreshJWTToken(@RequestHeader HttpHeaders headers, String username, String forceRefresh) {
		return jwtTokenProvider.refreshJWTToken(username, AppUtil.getTokenFromHeader(headers), forceRefresh);
	}

	@GetMapping(value = "/api/user/otp")
	public int getOTP(String username) {
		return appService.getOTP(username);
	}

	@PostMapping(value = "/api/user/adduser")
	public String addNewUser(@RequestHeader HttpHeaders headers, @RequestBody User newUser) {
		return appService.addNewUser(newUser);
	}

	@PostMapping(value = "/api/user/resetpassword")
	public String resetPassword(@RequestBody User resetUser) {
		return appService.resetPassword(resetUser);
	}

	@PutMapping(value = "/api/manage/user/save")
	public String saveUser(@RequestHeader HttpHeaders headers, @RequestBody User user) {
		return appService.saveUser(user, AppUtil.getLoggedUserFromHeader(headers));
	}
	
	@DeleteMapping(value = "/api/manage/user/delete")
	public String deleteUser(@RequestHeader HttpHeaders headers, @RequestBody User user) {
		return appService.deleteUser(user, AppUtil.getLoggedUserFromHeader(headers));
	}

}
