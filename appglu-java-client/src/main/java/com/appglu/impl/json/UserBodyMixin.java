package com.appglu.impl.json;

import org.codehaus.jackson.annotate.JsonProperty;

import com.appglu.User;

public abstract class UserBodyMixin {
	
	public UserBodyMixin(@JsonProperty("user") User user) {
		
	}

}
