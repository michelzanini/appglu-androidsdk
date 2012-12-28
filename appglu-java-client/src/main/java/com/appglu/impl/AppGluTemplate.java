package com.appglu.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.appglu.AnalyticsOperations;
import com.appglu.AppGluOperations;
import com.appglu.AsyncAnalyticsOperations;
import com.appglu.AsyncAppGluOperations;
import com.appglu.AsyncCrudOperations;
import com.appglu.AsyncPushOperations;
import com.appglu.AsyncSavedQueriesOperations;
import com.appglu.AsyncSyncOperations;
import com.appglu.AsyncUserOperations;
import com.appglu.CrudOperations;
import com.appglu.PushOperations;
import com.appglu.SavedQueriesOperations;
import com.appglu.SyncOperations;
import com.appglu.User;
import com.appglu.UserOperations;
import com.appglu.UserSessionPersistence;
import com.appglu.impl.json.JsonMessageConverterSelector;
import com.appglu.impl.util.StringUtils;

public class AppGluTemplate implements AppGluOperations, AsyncAppGluOperations {
	
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
	
	private AsyncAnalyticsOperations asyncAnalyticsOperations;
	
	private UserTemplate userOperations;
	
	private AsyncUserOperations asyncUserOperations;
	
	private SyncOperations syncOperations;
	
	private AsyncSyncOperations asyncSyncOperations;
	
	private HttpMessageConverter<Object> jsonMessageConverter;
	
	private DefaultHeadersHttpRequestInterceptor defaultHeadersHttpRequestInterceptor;
	
	private BasicAuthHttpRequestInterceptor basicAuthHttpRequestInterceptor;
	
	private UserSessionRequestInterceptor userSessionRequestInterceptor;
	
	private UserSessionPersistence userSessionPersistence;
	
	public AppGluTemplate(String baseUrl, String applicationKey, String applicationSecret) {
		if (StringUtils.isEmpty(baseUrl)) {
			throw new IllegalArgumentException("Base URL cannot be empty");
		}
		this.baseUrl = baseUrl;
		this.applicationKey = applicationKey;
		this.applicationSecret = applicationSecret;
		
		this.userSessionPersistence = new MemoryUserSessionPersistence();

		this.restTemplate = this.createRestTemplate();
		this.jsonMessageConverter = JsonMessageConverterSelector.getJsonMessageConverter();
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
	
	public void setDefaultHeaders(Map<String, List<String>> defaultHeaders) {
		this.checkInterceptorsAreInitialized();
		this.defaultHeadersHttpRequestInterceptor.setDefaultHeaders(defaultHeaders);
	}
	
	public Map<String, List<String>> getDefaultHeaders() {
		this.checkInterceptorsAreInitialized();
		return defaultHeadersHttpRequestInterceptor.getDefaultHeaders();
	}
	
	public void setUserSessionPersistence(UserSessionPersistence userSessionPersistence) {
		Assert.notNull(userSessionPersistence, "UserSessionPersistence param cannot be null.");
		
		if (this.userSessionPersistence.isUserAuthenticated()) {
			userSessionPersistence.saveSessionId(this.userSessionPersistence.getSessionId());
			userSessionPersistence.saveAuthenticatedUser(this.userSessionPersistence.getAuthenticatedUser());
		}
		
		this.userSessionPersistence = userSessionPersistence;
		this.userOperations.setUserSessionPersistence(this.userSessionPersistence);
		this.userSessionRequestInterceptor.setUserSessionPersistence(this.userSessionPersistence);
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
	
	public AsyncAnalyticsOperations asyncAnalyticsOperations() {
		this.checkAsyncExecutor();
		return asyncAnalyticsOperations;
	}
	
	public UserOperations userOperations() {
		return userOperations;
	}
	
	public AsyncUserOperations asyncUserOperations() {
		this.checkAsyncExecutor();
		return asyncUserOperations;
	}
	
	public SyncOperations syncOperations() {
		return syncOperations;
	}
	
	public AsyncSyncOperations asyncSyncOperations() {
		this.checkAsyncExecutor();
		return asyncSyncOperations;
	}

	public RestOperations restOperations() {
		return getRestTemplate();
	}
	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
	
	public boolean isUserAuthenticated() {
		return this.userSessionPersistence.isUserAuthenticated();
	}
	
	public String getSessionId() {
		return this.userSessionPersistence.getSessionId();
	}
	
	public User getAuthenticatedUser() {
		return this.userSessionPersistence.getAuthenticatedUser();
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
		this.userOperations = new UserTemplate(this.restOperations(), this.userSessionPersistence);
		this.syncOperations = new SyncTemplate(this.restOperations(), this.jsonMessageConverter);
	}
	
	private void initAsyncApis() {
		this.asyncCrudOperations = new AsyncCrudTemplate(this.asyncExecutor, this.crudOperations);
		this.asyncSavedQueriesOperations = new AsyncSavedQueriesTemplate(this.asyncExecutor, this.savedQueriesOperations);
		this.asyncPushOperations = new AsyncPushTemplate(this.asyncExecutor, this.pushOperations);
		this.asyncAnalyticsOperations = new AsyncAnalyticsTemplate(this.asyncExecutor, this.analyticsOperations);
		this.asyncUserOperations = new AsyncUserTemplate(this.asyncExecutor, this.userOperations);
		this.asyncSyncOperations = new AsyncSyncTemplate(this.asyncExecutor, this.syncOperations);
	}
	
	protected RestTemplate createRestTemplate() {
		if (ANDROID_ENVIRONMENT) {
			return new RestTemplate();
		}
		return new RestTemplate(ClientHttpRequestFactorySelector.getRequestFactory());
	}

	protected void configureHttpRequestInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
		this.defaultHeadersHttpRequestInterceptor = new DefaultHeadersHttpRequestInterceptor(this.baseUrl);
		this.basicAuthHttpRequestInterceptor = new BasicAuthHttpRequestInterceptor(this.applicationKey, this.applicationSecret);
		this.userSessionRequestInterceptor = new UserSessionRequestInterceptor(this.userSessionPersistence);
		
		interceptors.add(this.defaultHeadersHttpRequestInterceptor);
		interceptors.add(this.basicAuthHttpRequestInterceptor);
		interceptors.add(this.userSessionRequestInterceptor);
		
		if (ANDROID_ENVIRONMENT) {
			interceptors.add(new GZipHttpRequestInterceptor());
		}
	}

}