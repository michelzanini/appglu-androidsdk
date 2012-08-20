package com.appglu.impl.json;

import org.codehaus.jackson.annotate.JsonProperty;

public abstract class ErrorMixin {
	
	@JsonProperty("code")
	String code;
	
	@JsonProperty("message")
	String message;
	
}
