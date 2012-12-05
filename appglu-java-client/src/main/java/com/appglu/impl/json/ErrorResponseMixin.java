package com.appglu.impl.json;

import org.codehaus.jackson.annotate.JsonProperty;

import com.appglu.Error;

public abstract class ErrorResponseMixin {
	
	@JsonProperty("error")
	Error error;

}
