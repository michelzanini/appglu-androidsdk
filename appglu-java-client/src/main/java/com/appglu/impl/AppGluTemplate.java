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
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.appglu.AnalyticsOperations;
import com.appglu.AppGluOperations;
import com.appglu.AsyncCrudOperations;
import com.appglu.AsyncPushOperations;
import com.appglu.AsyncSavedQueriesOperations;
import com.appglu.CrudOperations;
import com.appglu.PushOperations;
import com.appglu.SavedQueriesOperations;
import com.appglu.impl.json.AppGluModule;
import com.appglu.impl.util.DateUtils;

public class AppGluTemplate implements AppGluOperations {
	
	private static final boolean ANDROID_ENVIRONMENT = ClassUtils.isPresent("android.os.Build", AppGluTemplate.class.getClassLoader());
	
	private String baseUrl;
	
	private String applicationKey;
	
	private String applicationSecret;
	
	private RestTemplate restTemplate;
	
	private AsyncExecutor asyncExecutor;
	
	private CrudOperations crudOperations;
	
	private AsyncCrudOperations asyncCrudOperations;
	
	private SavedQueriesOperations savedQueriesOperations;
	
	private AsyncSavedQueriesOperations asyncSavedQueriesOperations;
	
	private PushOperations pushOperations;
	
	private AsyncPushOperations asyncPushOperations;
	
	private AnalyticsOperations analyticsOperations;
	
	private HttpMessageConverter<Object> jsonMessageConverter;
	
	private DefaultHeadersHttpRequestInterceptor defaultHeadersHttpRequestInterceptor;
	
	private BasicAuthHttpRequestInterceptor basicAuthHttpRequestInterceptor;
	
	public AppGluTemplate(String baseUrl, String applicationKey, String applicationSecret) {
		if (!StringUtils.hasText(baseUrl)) {
			throw new IllegalArgumentException("Base URL cannot be empty");
		}
		this.baseUrl = baseUrl;
		this.applicationKey = applicationKey;
		this.applicationSecret = applicationSecret;

		this.restTemplate = this.createRestTemplate();
		this.jsonMessageConverter = this.createJsonMessageConverter();
		this.restTemplate.setMessageConverters(this.getMessageConverters());
		this.restTemplate.setErrorHandler(this.getResponseErrorHandler());
		this.restTemplate.setInterceptors(this.createInterceptors());
		
		this.initApis();
	}
	
	public void setAsyncExecutor(AsyncExecutor asyncExecutor) {
		Assert.notNull(asyncExecutor, "AsyncExecutor param cannot be null.");
		this.asyncExecutor = asyncExecutor;
		this.initAsyncApis();
	}
	
	private void checkAsyncExecutor() {
		if (asyncExecutor == null) {
			throw new IllegalStateException("AsyncExecutor is not initialized. It is requiered to call setAsyncExecutor() before using async operations.");
		}
	}

	private void checkInterceptorsAreInitialized() {
		if (this.defaultHeadersHttpRequestInterceptor == null) {
			throw new IllegalStateException("Http interceptors are not initialized. Did you override configureHttpRequestInterceptors() without calling super?");
		}
	}
	
	public void setDefaultHeaders(HttpHeaders defaultHeaders) {
		this.checkInterceptorsAreInitialized();
		this.defaultHeadersHttpRequestInterceptor.setDefaultHeaders(defaultHeaders);
	}
	
	public HttpHeaders getDefaultHeaders() {
		this.checkInterceptorsAreInitialized();
		return defaultHeadersHttpRequestInterceptor.getDefaultHeaders();
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}

	public String getApplicationKey() {
		return applicationKey;
	}

	public String getApplicationSecret() {
		return applicationSecret;
	}

	public CrudOperations crudOperations() {
		return crudOperations;
	}
	
	public AsyncCrudOperations asyncCrudOperations() {
		this.checkAsyncExecutor();
		return asyncCrudOperations;
	}

	public SavedQueriesOperations savedQueriesOperations() {
		return savedQueriesOperations;
	}
	
	public AsyncSavedQueriesOperations asyncSavedQueriesOperations() {
		this.checkAsyncExecutor();
		return asyncSavedQueriesOperations;
	}
	
	public PushOperations pushOperations() {
		return pushOperations;
	}
	
	public AsyncPushOperations asyncPushOperations() {
		this.checkAsyncExecutor();
		return asyncPushOperations;
	}
	
	public AnalyticsOperations analyticsOperations() {
		return analyticsOperations;
	}

	public RestOperations restOperations() {
		return getRestTemplate();
	}
	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
	
	private HttpMessageConverter<Object> createJsonMessageConverter() {
		MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		this.configureObjectMapper(objectMapper);
		converter.setObjectMapper(objectMapper);
		return converter;
	}
	
	private List<HttpMessageConverter<?>> getMessageConverters() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(this.jsonMessageConverter);
		return messageConverters;
	}
	
	private ResponseErrorHandler getResponseErrorHandler() {
		return new AppGluResponseErrorHandler(this.jsonMessageConverter);
	}
	
	private List<ClientHttpRequestInterceptor> createInterceptors() {
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		this.configureHttpRequestInterceptors(interceptors);
		return interceptors;
	}
	
	private void initApis() {
		this.crudOperations = new CrudTemplate(this.restOperations());
		this.savedQueriesOperations = new SavedQueriesTemplate(this.restOperations());
		this.pushOperations = new PushTemplate(this.restOperations());
		this.analyticsOperations = new AnalyticsTemplate(this.restOperations());
	}
	
	private void initAsyncApis() {
		this.asyncCrudOperations = new AsyncCrudTemplate(this.asyncExecutor, this.crudOperations);
		this.asyncSavedQueriesOperations = new AsyncSavedQueriesTemplate(this.asyncExecutor, this.savedQueriesOperations);
		this.asyncPushOperations = new AsyncPushTemplate(this.asyncExecutor, this.pushOperations);
	}
	
	protected RestTemplate createRestTemplate() {
		if (ANDROID_ENVIRONMENT) {
			return new RestTemplate();
		}
		return new RestTemplate(ClientHttpRequestFactorySelector.getRequestFactory());
	}
	
	protected void configureObjectMapper(ObjectMapper objectMapper) {
		objectMapper.registerModule(new AppGluModule());
		DateFormat dateFormat = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
		objectMapper.setDateFormat(dateFormat);
	}

	protected void configureHttpRequestInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
		this.defaultHeadersHttpRequestInterceptor = new DefaultHeadersHttpRequestInterceptor(this.baseUrl);
		this.basicAuthHttpRequestInterceptor = new BasicAuthHttpRequestInterceptor(this.applicationKey, this.applicationSecret);
		
		interceptors.add(this.defaultHeadersHttpRequestInterceptor);
		interceptors.add(this.basicAuthHttpRequestInterceptor);
		
		if (ANDROID_ENVIRONMENT) {
			interceptors.add(new GZipHttpRequestInterceptor());
		}
	}

}