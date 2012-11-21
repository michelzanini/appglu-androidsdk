package com.appglu.impl.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonPropertyOrder({"id", "username", "password"})
public abstract class UserMixin {
	
	@JsonProperty("id")
	@JsonSerialize(include=Inclusion.NON_NULL)
	Long id;
	
	@JsonProperty("username")
	String username;
	
	@JsonProperty("password")
	String password;

}