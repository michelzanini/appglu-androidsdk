package com.appglu.impl.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;

import com.appglu.AppGluRestClientException;
import com.appglu.impl.json.jackson.AppGluModule;
import com.appglu.impl.json.jackson.JacksonTableChangesJsonParser;
import com.appglu.impl.util.DateUtils;

public class JsonMessageConverterSelector {
	
	private static final boolean JACKSON_AVAILABLE =
		ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", RestTemplate.class.getClassLoader()) &&
		ClassUtils.isPresent("org.codehaus.jackson.JsonGenerator", RestTemplate.class.getClassLoader());
		
	public static HttpMessageConverter<Object> getJsonMessageConverter() {
		if (JACKSON_AVAILABLE) {
			return MappingJacksonHttpMessageConverterCreator.createJsonMessageConverter();
		}
		
		throw new AppGluRestClientException("No supported JSON parser library found on classpath");
	}
	
	public static TableChangesJsonParser getTableChangesJsonParser() {
		if (JACKSON_AVAILABLE) {
			return MappingJacksonHttpMessageConverterCreator.createTableChangesJsonParser();
		}
		
		throw new AppGluRestClientException("No supported JSON parser library found on classpath");
	}
	
	public static class MappingJacksonHttpMessageConverterCreator {
		
		private static ObjectMapper cachedObjectMapper;
		
		private static ObjectMapper createObjectMapper() {
			if (cachedObjectMapper == null) {
				ObjectMapper objectMapper = new ObjectMapper();
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