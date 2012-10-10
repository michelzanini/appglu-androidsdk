package com.appglu.impl.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.appglu.DevicePlatform;

@JsonPropertyOrder({"token", "alias", "platform", "appIdentifier"})
public abstract class DeviceMixin {

	@JsonProperty("token")
	String token;
	
	@JsonProperty("alias")
	String alias;
	
	@JsonProperty("platform")
	@JsonSerialize(using=DevicePlatformSerializer.class)
	@JsonDeserialize(using=DevicePlatformDeserializer.class)
	DevicePlatform platform;
	
	@JsonProperty("appIdentifier")
	String appIdentifier;
	
	@JsonIgnore
	abstract boolean isIos();
	
	@JsonIgnore
	abstract boolean isAndroid();
	
	private static class DevicePlatformSerializer extends JsonSerializer<DevicePlatform> {
		@Override
		public void serialize(DevicePlatform value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			if (value == null) {
				jgen.writeNull();
			} else {
				jgen.writeString(value.toString());
			}
		}
	}
	
	private static class DevicePlatformDeserializer extends JsonDeserializer<DevicePlatform> {
		@Override
		public DevicePlatform deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			return DevicePlatform.getDevicePlatform(jp.getText());
		}
	}
	
}