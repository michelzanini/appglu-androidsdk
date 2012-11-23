package com.appglu.impl.json;

import com.appglu.User;

public class UserBody {
	
	private User user;

	public UserBody(User user) {
		super();
		this.user = user;
	}
	
	public UserBody(String username, String password) {
		super();
		this.user = new User(username, password);
	}

	public User getUser() {
		return user;
	}

}