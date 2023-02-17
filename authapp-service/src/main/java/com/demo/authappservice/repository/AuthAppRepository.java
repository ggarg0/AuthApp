package com.demo.authappservice.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.authappservice.entity.User;
import com.demo.authappservice.util.AppUtil;


@Repository
public interface AuthAppRepository extends CrudRepository<User, Integer> {
	

	
	@Query("SELECT u FROM User u WHERE u.username = :username")
	List<User> loadUserDetails(@Param("username") String username);
	
	@Query("SELECT u FROM User u WHERE u.role = :role")
	List<User> loadUserByRole(@Param("role") String role);

	Optional<User> findByUsername(String username);
	

	/*
	public int addNewUser(User newUser) {
		int result = 0;
		int approved = 0;
		List<String> adminUser = new ArrayList<>();

		try {
			if (!newUser.getRole().equalsIgnoreCase("MANAGER") && !newUser.getRole().equalsIgnoreCase("ADMIN"))
				approved = 1;

			String firstNameString = newUser.getFirstName().contains("'") ? newUser.getFirstName().replaceAll("'", "''")
					: newUser.getFirstName();
			String lastNameString = newUser.getLastName().contains("'") ? newUser.getLastName().replaceAll("'", "''")
					: newUser.getLastName();

			newUser.setActive("1");
			newUser.setApproved(Integer.toString(approved));

			String sql = "INSERT INTO WFC_USERS VALUES ('" + firstNameString + "', '" + lastNameString + "', '"
					+ newUser.getUsername() + "', '" + newUser.getPassword() + "', '" + newUser.getTeam() + "', '"
					+ newUser.getRole() + "', " + approved + ", 1)";

	

			if (result == 1) {
				if (newUser.getApproved().equals("1")) {
					String messageBody = "Hello " + newUser.getFirstName() + " " + newUser.getLastName() + ",\n\n"
							+ "Access request for the application has been approved. " + "\n\nThanks";

					AppUtil.sendMail(newUser.getUsername(), null, "New user request", messageBody, true, false);
				} else {
					for (User admin : retrieveUserByRole("ADMIN"))
						adminUser.add(admin.getUsername());

					String messageBody = "Hello, \n\n" + newUser.getFirstName() + " " + newUser.getLastName()
							+ " has requested access as " + newUser.getRole() + " for the application. "
							+ "\nKindly review the request and provide the approval. \n\nThanks";

					AppUtil.sendMail(AppUtil.getStringFromList(adminUser, "; "), newUser.getUsername(),
							"User approval request", messageBody, false, false);
				}
				logger.info("New user added successfully : {}", newUser);
			} else {
				logger.info("New user {} not created", newUser.getUsername());
			}
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {
				result = -1;
				logger.error("New user {} signup exception - {} already exist", newUser.getUsername());
			} else {
				logger.error("New user {} signup exception - {}", newUser.getUsername(), e.getMessage());
			}
			return result;
		}
		return result;
	}


	public int resetPassword(User resetUser) {
		int result = 0;
		try {

			List<User> userList = new ArrayList();

			if (userList.isEmpty()) {
				result = 3;
				logger.info("User {} not found for password reset", resetUser.getUsername());
			} else {
				String updateWFCSQL = "UPDATE WFC_USERS SET PASSWORD = '" + resetUser.getPassword()
						+ "' WHERE USERNAME = '" + resetUser.getUsername() + "'";

			

				String messageBody = "Hello " + resetUser.getUsername() + ",\n\n"
						+ "Your password has been reset now. Please try to login. \n\nThanks";

				AppUtil.sendMail(resetUser.getUsername(), "", "App user password reset request", messageBody, false,
						false);

				logger.info("User password reset successful for " + resetUser.getUsername());
			}
		} catch (Exception e) {
			logger.error("User {} password reset exception - {}", resetUser.getUsername(), e.getMessage());
			return result;
		}
		return result;
	}
	*/
}


