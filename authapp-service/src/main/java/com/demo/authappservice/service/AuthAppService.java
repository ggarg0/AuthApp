package com.demo.authappservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.authappservice.entity.User;
import com.demo.authappservice.repository.AuthAppRepository;
import com.demo.authappservice.util.AppUtil;
import com.demo.authappservice.util.JwtUtil;

@Service
public class AuthAppService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuthAppRepository authRepository;

	@Autowired
	JwtUtil jwtTokenUtils;

	@Autowired
	OTPService otpService;

	public List<User> retrieveAllUsers(String team) {
		return authRepository.retrieveAllUsers(team);
	}

	public List<User> retrieveUserByRole(String role) {
		return authRepository.retrieveUserByRole(role);
	}

	public int addNewUser(User newUser) {
		int result = 0;
		try {
			logger.info("User add request for : {}", newUser.getUserName());
			if (otpService.getOtp(newUser.getUserName()) != 0
					&& otpService.getOtp(newUser.getUserName()) == Integer.parseInt(newUser.getOTP())) {
				result = authRepository.addNewUser(newUser);

				if (result == 1 || result == -1)
					otpService.clearOTP(newUser.getUserName());

			} else {
				logger.info("OTP mismatch found for user {} for user signup", newUser.getUserName());
			}
		} catch (Exception e) {
			logger.error("New user add request for {} : Exception - {}", newUser.getUserName(), e.getMessage());
			String messageBody = "Hello " + newUser.getFirstName() + " " + newUser.getLastName() + ",\n\n"
					+ "Access request for application is not completed successfully. "
					+ "Please share error details with admin.\n\nThanks";

			AppUtil.sendMail(newUser.getUserName(), null, "New user request", messageBody, true, false);
		}
		return result;
	}

	public int saveUser(User user, String loggedUser) {
		int result = 0;
		try {
			result = authRepository.saveUser(user);
			if (result == 1) {
				String messageBody = "Hello," + " \n\nAccess request for application has been modified:"
						+ "\n\nRole : " + user.getRole() + "\nApproved : " + user.getApproved() + "\nActive : "
						+ user.getActive() + "\nTeam : " + user.getTeam() + "\n\nThanks";

				AppUtil.sendMail(user.getUserName(), null, "User access request", messageBody, true,
						false);
				logger.info("User " + user.getUserName() + " access request modified by " + loggedUser + " => Role : "
						+ user.getRole() + ", Approved : " + user.getApproved() + " and Active : " + user.getActive());
			} else {
				logger.info("User " + user.getUserName() + " not saved");
			}
		} catch (Exception e) {
			logger.error("User save request for " + user.getUserName() + " : Exception - " + e.getMessage());
		}
		return result;
	}

	public List<String> refreshJWTToken(String userName, String tokenFromHeader) {
		List<String> refreshToken = new ArrayList<String>();
		try {
			long differenceInMinutes = (jwtTokenUtils.extractExpiration(tokenFromHeader).getTime()
					- new Date().getTime()) / 60000;
			if (differenceInMinutes < 5) {
				refreshToken.add(jwtTokenUtils.generateToken(userName));
				logger.info("JWT token refresh for " + userName);
			} else {
				refreshToken.add(tokenFromHeader);
			}
		} catch (Exception e) {
			logger.error("JWT token refresh for " + userName + " : Exception - " + e.getMessage());
		}
		return refreshToken;
	}

	public int getOTP(String username) {
		int result = 0;
		try {
			otpService.generateOTP(username);
			if (otpService.getOtp(username) != 0) {
				String messageBody = "Hello " + username + ",\n\nYour OTP " + otpService.getOtp(username)
						+ " is valid for 15 minutes. \n\nThanks";
				AppUtil.sendMail(username, "", "User OTP request", messageBody, false, false);
				logger.info("User OTP sent successfully for " + username);
				result = 1;
			} else {
				logger.info("User OTP not generated for " + username);
			}
		} catch (Exception e) {
			logger.error("User OTP for " + username + " : Exception - " + e.getMessage());
		}
		return result;
	}

	public int resetPassword(User resetUser) {
		int result = 0;
		try {
			logger.info("User reset password request for " + resetUser.getUserName());
			if (otpService.getOtp(resetUser.getUserName()) != 0
					&& otpService.getOtp(resetUser.getUserName()) == Integer.parseInt(resetUser.getOTP())) {
				result = authRepository.resetPassword(resetUser);

				if (result == 1)
					otpService.clearOTP(resetUser.getUserName());

			} else {
				result = 2;
				logger.info("OTP mismatch found for user {} for password reset", resetUser.getUserName());
			}
		} catch (Exception e) {
			logger.error("User password reset for " + resetUser.getUserName() + " : Exception - " + e.getMessage());
		}
		return result;
	}
}
