package com.appglu.impl.json.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
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
	@JsonDeserialize(using=NullDeserializer.class)
	String password;
	
	private static class NullDeserializer extends JsonDeserializer<String> {
		@Override
		public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			return null;
		}
	}
	
}