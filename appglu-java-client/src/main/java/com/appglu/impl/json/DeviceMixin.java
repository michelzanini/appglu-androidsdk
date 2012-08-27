package com.appglu.impl.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.appglu.DevicePlatform;

@JsonPropertyOrder({"token", "alias", "platform"})
public abstract class DeviceMixin {

	@JsonProperty("token")
	String token;
	
	@JsonProperty("alias")
	String alias;
	
	@JsonProperty("platform")
	@JsonDeserialize(using=DevicePlatformDeserializer.class)
	DevicePlatform platform;
	
	@JsonIgnore
	abstract boolean isIos();
	
	@JsonIgnore
	abstract boolean isAndroid();
	
	private static class DevicePlatformDeserializer extends JsonDeserializer<DevicePlatform> {
		@Override
		public DevicePlatform deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			return DevicePlatform.getDevicePlatform(jp.getText());
		}
	}
	
}