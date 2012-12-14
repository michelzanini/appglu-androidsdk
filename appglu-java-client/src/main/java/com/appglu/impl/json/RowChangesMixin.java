package com.appglu.impl.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.appglu.SyncOperation;

public abstract class RowChangesMixin {
	
	@JsonProperty("appglu_key")
	long syncKey;
	
	@JsonProperty("appglu_sync_operation")
	@JsonDeserialize(using=AppgluSyncOperationDeserializer.class)
	SyncOperation syncOperation;
	
	@JsonAnySetter
	abstract void addRowProperty(String key, Object value);
	
	private static class AppgluSyncOperationDeserializer extends JsonDeserializer<SyncOperation> {
		@Override
		public SyncOperation deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			return SyncOperation.getSyncOperation(jp.getText());
		}
	}

}