package com.appglu.impl.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.appglu.ErrorCode;

public abstract class ErrorMixin {
	
	@JsonProperty("code")
	@JsonDeserialize(using=ErrorCodeDeserializer.class)
	ErrorCode code;
	
	@JsonProperty("message")
	String message;
	
	private static class ErrorCodeDeserializer extends JsonDeserializer<ErrorCode> {
		@Override
		public ErrorCode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			return ErrorCode.getErrorCode(jp.getText());
		}
	}
	
}