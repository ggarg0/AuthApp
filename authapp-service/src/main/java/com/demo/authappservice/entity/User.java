package com.demo.authappservice.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class User implements UserDetails {

	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private String team;
	private String role;
	private String approved;
	private String active;
	private String otp;
	private String message;

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", userName=" + userName + "" + ", team="
				+ team + ", role=" + role + ", approved=" + approved + ", active=" + active + "]";
	}

	public User() {
	}

	public User(String firstName, String lastName, String userName, String password, String team, String role,
			String approved, String active, String otp, String message) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.team = team;
		this.role = role;
		this.approved = approved;
		this.active = active;
		this.otp = otp;
		this.message = message;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getTeam() {
		return team;
	}

	public String getRole() {
		return role;
	}

	public String getApproved() {
		return approved;
	}

	public String getActive() {
		return active;
	}

	public String getOTP() {
		return otp;
	}

	public String getMessage() {
		return message;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public void setOTP(String otp) {
		this.otp = otp;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_".concat(getRole().toUpperCase())));
	}
}
