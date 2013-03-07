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
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.appglu.ErrorCode;

@JsonPropertyOrder({"code", "message"})
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
