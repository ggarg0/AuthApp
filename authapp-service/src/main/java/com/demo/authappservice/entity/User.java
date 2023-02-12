package com.demo.authappservice.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userid;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	private String team;
	private String role;
	private String approved;
	private String active;
	private String otp;
	private String message;

	@Override
	public String toString() {
		return "User [firstName=" + firstname + ", lastname=" + lastname + ", username=" + username + "" + ", team="
				+ team + ", role=" + role + ", approved=" + approved + ", active=" + active + "]";
	}

	public User() {
	}

	public User(String firstname, String lastname, String username, String password, String team, String role,
			String approved, String active, String otp, String message) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
		this.team = team;
		this.role = role;
		this.approved = approved;
		this.active = active;
		this.otp = otp;
		this.message = message;
	}

	public String getFirstName() {
		return firstname;
	}

	public String getLastName() {
		return lastname;
	}

	public String getUserName() {
		return username;
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

	public void setFirstName(String firstname) {
		this.firstname = firstname;
	}

	public void setLastName(String lastname) {
		this.lastname = lastname;
	}

	public void setUserName(String username) {
		this.username = username;
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
		return username;
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
