package com.appglu.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.appglu.Appglu;
import com.appglu.CrudOperations;
import com.appglu.SavedQueriesOperations;
import com.appglu.impl.json.AppgluModule;

public class AppgluTemplate implements Appglu {
	
	static final String APPGLU_API_URL = "http://localhost:8080/appglu";
	
	private RestTemplate restTemplate;
	
	private CrudOperations crudOperations;
	
	private SavedQueriesOperations savedQueriesOperations;
	
	private HttpMessageConverter<Object> jsonMessageConverter;
	
	public AppgluTemplate(String applicationKey, String applicationSecret) {
		this.restTemplate = this.createRestTemplate(applicationKey, applicationSecret);
		this.jsonMessageConverter = this.createJsonMessageConverter();
		this.restTemplate.setMessageConverters(getMessageConverters());
		this.restTemplate.setErrorHandler(getResponseErrorHandler());
		this.initSubApis();
	}

	public CrudOperations crudOperations() {
		return crudOperations;
	}
	
	public SavedQueriesOperations savedQueriesOperations() {
		return savedQueriesOperations;
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
		objectMapper.registerModule(new AppgluModule());
		converter.setObjectMapper(objectMapper);
		return converter;
	}
	
	protected RestTemplate createRestTemplate(String key, String secret) {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(key, secret));
		httpClient.setCredentialsProvider(credentialsProvider);
		requestFactory.setHttpClient(httpClient);
		
		return new RestTemplate(requestFactory);
	}
	
	protected List<HttpMessageConverter<?>> getMessageConverters() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(this.jsonMessageConverter);
		return messageConverters;
	}

	protected ResponseErrorHandler getResponseErrorHandler() {
		return new AppgluResponseErrorHandler(this.jsonMessageConverter);
	}

	private void initSubApis() {
		this.crudOperations = new CrudTemplate(this.restOperations());
		this.savedQueriesOperations = new SavedQueriesTemplate(this.restOperations());
	}

}