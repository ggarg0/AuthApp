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
import org.springframework.web.bind.annotation.PathVariable;
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
	private JwtTokenProvider tokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/api/manage/users/{team}")
	public List<User> retrieveAllUsers(@PathVariable String team) {
		return appService.retrieveAllUsers(team);
	}

	@PostMapping(value = "/api/manage/user/authenticate")
	public List<User> authenticate(@RequestBody User user) {
		List<User> userList = new ArrayList<User>();
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
			if (authentication.isAuthenticated()) {
				User userAuth = (User) authentication.getPrincipal();
				userAuth.setPassword("");
				userAuth.setMessage(tokenProvider.createToken(userAuth.getUsername(), userAuth.getRole()));
				userList.add(userAuth);
				logger.info("User {} login as {}", userAuth.getUserName(), userAuth.getRole());
				boolean loginError = !(Integer.parseInt(userAuth.getActive()) == 1
						&& Integer.parseInt(userAuth.getApproved()) == 1);
				
			}
		} catch (Exception e) {
			logger.error("Authentication error for " + user.getUserName() + " : Exception - " + e.getMessage());
	
		}
		return userList;
	}

	@GetMapping(value = "/api/ce/user/refreshjwttoken/{username}/{userrole}")
	public List<String> refreshJWTToken(@RequestHeader HttpHeaders headers, @PathVariable String username,
			@PathVariable String userrole) {
		return tokenProvider.refreshJWTToken(username, userrole, AppUtil.getTokenFromHeader(headers));
	}

	@GetMapping(value = "/api/manage/user/otp/{username}")
	public int getOTP(@PathVariable String username) {
		return appService.getOTP(username);
	}

	@PostMapping(value = "/api/manage/user/resetpassword")
	public int resetPassword(@RequestBody User resetUser) {
		return appService.resetPassword(resetUser);
	}

	@PostMapping(value = "/api/manage/user/adduser")
	public int addNewUser(@RequestHeader HttpHeaders headers, @RequestBody User newUser) {
		return appService.addNewUser(newUser);
	}

	@PutMapping(value = "/api/manage/user/save")
	public int saveUser(@RequestHeader HttpHeaders headers, @RequestBody User user) {
		return appService.saveUser(user, AppUtil.getLoggedUserFromHeader(headers));
	}

}
