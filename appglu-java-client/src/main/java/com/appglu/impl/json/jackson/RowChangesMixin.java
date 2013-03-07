/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu.impl.json.jackson;

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
