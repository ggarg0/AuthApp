package com.demo.authappservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<User>> loadUserDetails(String username) {
		List<User> user = new ArrayList<User>();
		user.add((userService.loadUserDetails(username)));

		if (user.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/api/manage/users")
	public ResponseEntity<List<User>> retrieveAllUsers() {
		List<User> list = userService.retrieveAllUsers();

		if (list.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@PostMapping(value = "/api/user/authenticate")
	public ResponseEntity<User> authenticate(@RequestBody User user) {
		User userAuth = new User();
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		if (authentication.isAuthenticated()) {
			userAuth = (User) authentication.getPrincipal();
			userAuth.setPassword("");
			userAuth.setMessage(jwtTokenProvider.generateToken(userAuth.getUsername()));
			logger.info("User {} login as {}", userAuth.getUsername(), userAuth.getRole());
		}
		return new ResponseEntity<>(userAuth, HttpStatus.OK);
	}

	@GetMapping(value = "/api/user/refreshjwttoken")
	public ResponseEntity<String> refreshJWTToken(@RequestHeader HttpHeaders headers, String username,
			String forceRefresh) {
		return new ResponseEntity<>(
				jwtTokenProvider.refreshJWTToken(username, AppUtil.getTokenFromHeader(headers), forceRefresh),
				HttpStatus.OK);
	}

	@GetMapping(value = "/api/user/otp")
	public ResponseEntity<String> getOTP(String username) {
		return new ResponseEntity<>(userService.getOTP(username), HttpStatus.OK);
	}

	@PostMapping(value = "/api/user/adduser")
	public ResponseEntity<User> addNewUser(@RequestHeader HttpHeaders headers, @RequestBody User newUser) {
		return new ResponseEntity<>(userService.addNewUser(newUser), HttpStatus.OK);
	}

	@PostMapping(value = "/api/user/resetpassword")
	public ResponseEntity<String> resetPassword(@RequestBody User resetUser) {
		userService.resetPassword(resetUser);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping(value = "/api/manage/user/save")
	public ResponseEntity<User> saveUser(@RequestHeader HttpHeaders headers, @RequestBody User user) {
		return new ResponseEntity<>(userService.saveUser(user, AppUtil.getLoggedUserFromHeader(headers)),
				HttpStatus.OK);
	}

	@DeleteMapping(value = "/api/manage/user/delete")
	ResponseEntity<HttpStatus> deleteUser(@RequestHeader HttpHeaders headers, String username) {
		userService.deleteUser(username, AppUtil.getLoggedUserFromHeader(headers));
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
