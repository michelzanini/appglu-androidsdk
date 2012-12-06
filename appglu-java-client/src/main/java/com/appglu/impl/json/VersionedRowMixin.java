package com.appglu.impl.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.appglu.AppgluSyncOperation;

public abstract class VersionedRowMixin {
	
	@JsonProperty("appglu_key")
	long appgluKey;
	
	@JsonProperty("appglu_sync_operation")
	@JsonDeserialize(using=AppgluSyncOperationDeserializer.class)
	AppgluSyncOperation appgluSyncOperation;
	
	@JsonAnySetter
	abstract void addRowProperty(String key, Object value);
	
	private static class AppgluSyncOperationDeserializer extends JsonDeserializer<AppgluSyncOperation> {
		@Override
		public AppgluSyncOperation deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			return AppgluSyncOperation.getAppgluSyncOperation(jp.getText());
		}
	}

}