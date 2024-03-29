package com.demo.authappservice.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.demo.authappservice.constant.MessageConstants;
import com.demo.authappservice.entity.User;
import com.demo.authappservice.exception.OTPMismatchException;
import com.demo.authappservice.exception.UserAlreadyExistsException;
import com.demo.authappservice.exception.UserNotFoundException;
import com.demo.authappservice.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	OTPService otpService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<User> retrieveAllUsers() {
		List<User> result = new ArrayList<>();
		this.userRepository.findAll().forEach(user -> {
			result.add(user);
		});
		return result;
	}

	public User loadUserDetails(String username) {
		User user = this.userRepository.loadUserDetails(username);
		if (user == null)
			throw new UserNotFoundException(MessageConstants.UserNotFound);

		return user;
	}

	public User addNewUser(User newUser) {
		User user = null;
		List<String> adminUser = new ArrayList<>();
		try {
			if (Objects.nonNull(newUser) && otpService.getOtp(newUser.getUsername()) != 0
					&& otpService.getOtp(newUser.getUsername()) == Integer.parseInt(newUser.getOTP())) {

				logger.info("User add request for : {}", newUser.getUsername());

				if (newUser.getRole().equalsIgnoreCase(MessageConstants.ADMIN))
					newUser.setApproved(MessageConstants.NO);
				else
					newUser.setApproved(MessageConstants.YES);

				newUser.setActive(MessageConstants.YES);
				user = userRepository.save(newUser);

				if (user.getApproved().equalsIgnoreCase(MessageConstants.YES)) {
					String messageBody = "Hello " + user.getFirstname() + " " + user.getLastname() + ",\n\n"
							+ "Access request for the application has been approved. " + "\n\nThanks";

					// AppUtil.sendMail(user.getUsername(), null, "New user request", messageBody,
					// true, false);
				} else {
					for (User admin : userRepository.loadUserByRole(MessageConstants.ADMIN))
						adminUser.add(admin.getUsername());

					String messageBody = "Hello, \n\n" + user.getFirstname() + " " + user.getLastname()
							+ " has requested access as " + user.getRole() + " for the application. "
							+ "\nKindly review the request and provide the approval. \n\nThanks";

					// AppUtil.sendMail(AppUtil.getStringFromList(adminUser, "; "),
					// user.getUsername(),
					// "User approval request", messageBody, false, false);
				}

				otpService.clearOTP(newUser.getUsername());
				logger.info("New user added successfully : {}", newUser);
			} else {
				logger.info("OTP mismatch found for user {} for user signup", newUser.getUsername());
				throw new OTPMismatchException(MessageConstants.OTPMismatchFound);
			}
		} catch (DataIntegrityViolationException e) {
			logger.error("New user {} signup exception - {} already exist", newUser.getUsername());
			throw new UserAlreadyExistsException(MessageConstants.UserAlreadyExist);
		}
		return user;
	}

	public User saveUser(User userDetails, String loggedUser) {
		User user = userRepository.loadUserDetails(userDetails.getUsername());
		user.setFirstname(userDetails.getFirstname());
		user.setLastname(userDetails.getLastname());
		user.setTeam(userDetails.getTeam());
		user.setRole(userDetails.getRole());
		user.setActive(userDetails.getActive());
		user.setApproved(userDetails.getApproved());
		userRepository.save(user);

		String messageBody = "Hello," + " \n\nAccess request for application has been modified:" + "\n\nRole : "
				+ user.getRole() + "\nApproved : " + user.getApproved() + "\nActive : " + user.getActive() + "\nTeam : "
				+ user.getTeam() + "\n\nThanks";

		// AppUtil.sendMail(user.getUsername(), null, "User access request",
		// messageBody, true, false);

		logger.info("User " + user.getUsername() + " access request modified by " + loggedUser + " => Role : "
				+ user.getRole() + ", Approved : " + user.getApproved() + " and Active : " + user.getActive());

		return user;
	}

	public void deleteUser(String username, String loggedUser) {
		User user = userRepository.loadUserDetails(username);
		userRepository.delete(user);
		String messageBody = "Hello," + " \n\nAccess request for application has been modified:" + "\n\nRole : "
				+ user.getRole() + "\nApproved : " + user.getApproved() + "\nActive : " + user.getActive() + "\nTeam : "
				+ user.getTeam() + "\n\nThanks";

		// AppUtil.sendMail(user.getUsername(), null, "User access request",
		// messageBody, true, false);

		logger.info("User " + user.getUsername() + " deleted by " + loggedUser);
	}

	public void resetPassword(User resetUser) {
		logger.info("User reset password request for " + resetUser.getUsername());
		User user = userRepository.loadUserDetails(resetUser.getUsername());

		if (Objects.isNull(user))
			throw new UserNotFoundException(MessageConstants.UserNotFound);

		if (otpService.getOtp(resetUser.getUsername()) == 0
				|| otpService.getOtp(resetUser.getUsername()) != Integer.parseInt(resetUser.getOTP()))
			throw new OTPMismatchException(MessageConstants.OTPMismatchFound);

		user.setPassword(resetUser.getPassword());
		userRepository.save(user);
		String messageBody = "Hello " + user.getUsername() + ",\n\n"
				+ "Your password has been reset now. Please try to login. \n\nThanks";

		// AppUtil.sendMail(user.getUsername(), "", "App user password reset
		// request", messageBody, false, false);

		logger.info("User password reset successful for {}", user.getUsername());
		otpService.clearOTP(user.getUsername());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
		Optional<User> userInfo = userRepository.findByUsername(username);
		return userInfo.orElseThrow(() -> new UserNotFoundException(MessageConstants.UserNotFound + " : " + username));

	}

	public Collection<GrantedAuthority> getGrantedAuthority(User user) {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		if (user.getRole().equalsIgnoreCase(MessageConstants.ADMIN))
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		if (user.getRole().equalsIgnoreCase(MessageConstants.DEVELOPER))
			authorities.add(new SimpleGrantedAuthority("ROLE_DEVELOPER"));

		return authorities;
	}

	public String getOTP(String username) {
		int result = 0;

		if (Objects.isNull(username) || username.isBlank())
			throw new UserNotFoundException(MessageConstants.UserNotFound);

		otpService.generateOTP(username);

		if (otpService.getOtp(username) != 0) {
			String messageBody = "Hello " + username + ",\n\nYour OTP " + otpService.getOtp(username)
					+ " is valid for 15 minutes. \n\nThanks";
			// AppUtil.sendMail(username, "", "User OTP request", messageBody, false,
			// false);
			logger.info("User OTP sent successfully for " + username);
			result = otpService.getOtp(username);
		} else {
			logger.info("User OTP not generated for " + username);
		}
		return String.valueOf(result);
	}
}
