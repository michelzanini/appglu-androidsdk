package com.appglu.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.appglu.Appglu;
import com.appglu.CrudOperations;
import com.appglu.PushOperations;
import com.appglu.SavedQueriesOperations;
import com.appglu.impl.json.AppgluModule;
import com.appglu.impl.util.DateUtils;

public class AppgluTemplate implements Appglu {
	
	private String baseUrl;
	
	private HttpHeaders defaultHeaders;
	
	private String applicationKey;
	
	private String applicationSecret;
	
	private RestTemplate restTemplate;
	
	private CrudOperations crudOperations;
	
	private SavedQueriesOperations savedQueriesOperations;
	
	private PushOperations pushOperations;
	
	private HttpMessageConverter<Object> jsonMessageConverter;
	
	public AppgluTemplate(String baseUrl, String applicationKey, String applicationSecret) {
		this(baseUrl, new HttpHeaders(), applicationKey, applicationSecret);
	}
	
	public AppgluTemplate(String baseUrl, HttpHeaders defaultHeaders, String applicationKey, String applicationSecret) {
		if (!StringUtils.hasText(baseUrl)) {
			throw new IllegalArgumentException("Base URL cannot be empty");
		}
		this.baseUrl = baseUrl;
		this.defaultHeaders = defaultHeaders;
		this.applicationKey = applicationKey;
		this.applicationSecret = applicationSecret;

		this.restTemplate = this.createRestTemplate();
		this.jsonMessageConverter = this.createJsonMessageConverter();
		this.restTemplate.setMessageConverters(this.getMessageConverters());
		this.restTemplate.setErrorHandler(this.getResponseErrorHandler());
		this.restTemplate.setInterceptors(this.createInterceptors());
		
		this.initSubApis();
	}
	
	public CrudOperations crudOperations() {
		return crudOperations;
	}
	
	public SavedQueriesOperations savedQueriesOperations() {
		return savedQueriesOperations;
	}
	
	public PushOperations pushOperations() {
		return pushOperations;
	}

	public RestOperations restOperations() {
		return getRestTemplate();
	}
	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
	
	protected HttpMessageConverter<Object> createJsonMessageConverter() {
		MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		this.configureObjectMapper(objectMapper);
		converter.setObjectMapper(objectMapper);
		return converter;
	}
	
	protected void configureObjectMapper(ObjectMapper objectMapper) {
		objectMapper.registerModule(new AppgluModule());
		DateFormat dateFormat = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
		objectMapper.setDateFormat(dateFormat);
	}

	protected List<HttpMessageConverter<?>> getMessageConverters() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(this.jsonMessageConverter);
		return messageConverters;
	}
	
	protected RestTemplate createRestTemplate() {
		return new RestTemplate();
	}

	private ResponseErrorHandler getResponseErrorHandler() {
		return new AppgluResponseErrorHandler(this.jsonMessageConverter);
	}
	
	private List<ClientHttpRequestInterceptor> createInterceptors() {
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		this.configureHttpRequestInterceptors(interceptors);
		return interceptors;
	}

	protected void configureHttpRequestInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
		interceptors.add(new DefaultHeadersHttpRequestInterceptor(this.baseUrl, this.defaultHeaders));
		interceptors.add(new BasicAuthHttpRequestInterceptor(this.applicationKey, this.applicationSecret));
	}

	private void initSubApis() {
		this.crudOperations = new CrudTemplate(this.restOperations());
		this.savedQueriesOperations = new SavedQueriesTemplate(this.restOperations());
		this.pushOperations = new PushTemplate(this.restOperations());
	}

}