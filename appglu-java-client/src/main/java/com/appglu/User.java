package com.appglu;

import java.io.Serializable;

/**
 * Represents a mobile application user.<br>
 * When the {@code User} is authenticated, this object will be saved using a {@link UserSessionPersistence} implementation.
 * 
 * @see UserSessionPersistence
 * @since 1.0.0
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String username;
	
	private String password;
	
	public User() {
		
	}
	
	public User(String username) {
		this.username = username;
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString() {
		return "User [username=" + username + "]";
	}
	
}