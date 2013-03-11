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
package com.appglu.impl.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.ClassUtils;

import com.appglu.AppGluRestClientException;
import com.appglu.impl.json.jackson.AppGluModule;
import com.appglu.impl.json.jackson.JacksonTableChangesJsonParser;
import com.appglu.impl.util.DateUtils;

public class JsonMessageConverterSelector {
	
	private static final boolean JACKSON_AVAILABLE =
		ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", JsonMessageConverterSelector.class.getClassLoader()) &&
		ClassUtils.isPresent("org.codehaus.jackson.JsonGenerator", JsonMessageConverterSelector.class.getClassLoader());
		
	public static HttpMessageConverter<Object> getJsonMessageConverter() {
		if (JACKSON_AVAILABLE) {
			return JacksonHttpMessageConverterCreator.createJsonMessageConverter();
		}
		
		throw new AppGluRestClientException("No supported JSON parser library found on classpath");
	}
	
	public static TableChangesJsonParser getTableChangesJsonParser() {
		if (JACKSON_AVAILABLE) {
			return JacksonHttpMessageConverterCreator.createTableChangesJsonParser();
		}
		
		throw new AppGluRestClientException("No supported JSON parser library found on classpath");
	}
	
	public static class JacksonHttpMessageConverterCreator {
		
		private static ObjectMapper cachedObjectMapper;
		
		private static ObjectMapper createObjectMapper() {
			if (cachedObjectMapper == null) {
				ObjectMapper objectMapper = new ObjectMapper();
				
				/* If we encounter a not expected JSON property then we must ignore it */
				objectMapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				
				objectMapper.registerModule(new AppGluModule());
				DateFormat dateFormat = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
				objectMapper.setDateFormat(dateFormat);
				
				cachedObjectMapper = objectMapper;
			}
			return cachedObjectMapper;
		}

		public static HttpMessageConverter<Object> createJsonMessageConverter() {
			MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
			converter.setObjectMapper(createObjectMapper());
			return converter;
		}

		public static TableChangesJsonParser createTableChangesJsonParser() {
			return new JacksonTableChangesJsonParser(createObjectMapper());
		}
		
	}
	
}
