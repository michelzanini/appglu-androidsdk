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
import com.appglu.AsyncStorageOperations;
import com.appglu.AsyncSyncOperations;
import com.appglu.AsyncUserOperations;
import com.appglu.CrudOperations;
import com.appglu.PushOperations;
import com.appglu.SavedQueriesOperations;
import com.appglu.StorageOperations;
import com.appglu.SyncOperations;
import com.appglu.User;
import com.appglu.UserOperations;
import com.appglu.UserSessionPersistence;
import com.appglu.impl.json.JsonMessageConverterSelector;
import com.appglu.impl.util.StringUtils;

/**
 * <strong>The central class for AppGlu Java Client SDK.</strong>
 * 
 * <p>This is always going to be the starting point for doing anything with the AppGlu Java Client SDK.<br>
 * To initialized it, you will need your credentials to authenticate your account with AppGlu:
 * 
 * <p><code>
 * AppGluTemplate appGluTemplate = new AppGluTemplate("appKey", "appSecret");
 * </code>
 * 
 * <p>After {@code AppGluTemplate} is initialized you will have access to AppGlu operations classes. See the table below:
 * 
 * <p><table>
 * <tr><th>Synchronous Operations</th><th>Asynchronous Operations</th><th>API class description</th></tr>
 * <tr><td>{@link #crudOperations()}</td><td>{@link #asyncCrudOperations()}</td><td>{@link CrudOperations} contains create, read, update and delete operations to be applied on your tables</td></tr>
 * <tr><td>{@link #savedQueriesOperations()}</td><td>{@link #asyncSavedQueriesOperations()}</td><td>{@link SavedQueriesOperations} allow you to run your previously created SQLs on AppGlu and obtain the results</td></tr>
 * <tr><td>{@link #pushOperations()}</td><td>{@link #asyncPushOperations()}</td><td>{@link PushOperations} is used to register / unregister the device making it eligible to receive push notifications</td></tr>
 * <tr><td>{@link #analyticsOperations()}</td><td>{@link #asyncAnalyticsOperations()}</td><td>{@link AnalyticsOperations} is used to log events to AppGlu allowing it to collect mobile app usage statistics</td></tr>
 * <tr><td>{@link #userOperations()}</td><td>{@link #asyncUserOperations()}</td><td>{@link UserOperations} has methods to login / logout and sign up new mobile app users</td></tr>
 * <tr><td>{@link #syncOperations()}</td><td>{@link #asyncSyncOperations()}</td><td>{@link SyncOperations} is used to synchronize the data in your local SQLite tables with the AppGlu server</td></tr>
 * <tr><td>{@link #storageOperations()}</td><td>{@link #asyncStorageOperations()}</td><td>{@link StorageOperations} has methods to download and cache files from AppGlu server</td></tr>
 * </table>
 * 
 * @since 1.0.0
 */
public class AppGluTemplate implements AppGluOperations, AsyncAppGluOperations {
	
	public static final String DEFAULT_BASE_URL = "https://api.appglu.com";
	
	public static final String DEFAULT_ENVIRONMENT = "production";
	
	private static final boolean ANDROID_ENVIRONMENT = ClassUtils.isPresent("android.os.Build", AppGluTemplate.class.getClassLoader());
	
	private String baseUrl;
	
	private String applicationKey;
	
	private String applicationSecret;
	
	private String applicationEnvironment;
	
	private RestTemplate restTemplate;
	
	private RestTemplate downloadRestTemplate;
	
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
	
	private StorageOperations storageOperations;
	
	private AsyncStorageOperations asyncStorageOperations;
	
	private HttpMessageConverter<Object> jsonMessageConverter;
	
	private DefaultHeadersHttpRequestInterceptor defaultHeadersHttpRequestInterceptor;
	
	private BasicAuthHttpRequestInterceptor basicAuthHttpRequestInterceptor;
	
	private UserSessionRequestInterceptor userSessionRequestInterceptor;
	
	private UserSessionPersistence userSessionPersistence;
	
	/**
	 * @param applicationKey a randomly generated unique key specific for each mobile application
	 * @param applicationSecret the secret key used to authenticate this application
	 */
	public AppGluTemplate(String applicationKey, String applicationSecret) {
		this(DEFAULT_BASE_URL, applicationKey, applicationSecret);
	}
	
	/**
	 * @param baseUrl the server URL to point to, if different from the default {@link AppGluTemplate#DEFAULT_BASE_URL}
	 * @param applicationKey a randomly generated unique key specific for each mobile application
	 * @param applicationSecret the secret key used to authenticate this application
	 */
	public AppGluTemplate(String baseUrl, String applicationKey, String applicationSecret) {
		this(baseUrl, applicationKey, applicationSecret, DEFAULT_ENVIRONMENT);
	}
	
	/**
	 * @param baseUrl the server URL to point to, if different from the default {@link AppGluTemplate#DEFAULT_BASE_URL}
	 * @param applicationKey a randomly generated unique key specific for each mobile application
	 * @param applicationSecret the secret key used to authenticate this application
	 * @param applicationEnvironment The environment name to be accessed. Normally set to "staging" or "production", if not set "production" is assumed
	 */
	public AppGluTemplate(String baseUrl, String applicationKey, String applicationSecret, String applicationEnvironment) {
		if (StringUtils.isEmpty(baseUrl)) {
			throw new IllegalArgumentException("Base URL cannot be empty");
		}
		
		this.baseUrl = baseUrl;
		this.applicationKey = applicationKey;
		this.applicationSecret = applicationSecret;
		this.applicationEnvironment = applicationEnvironment;
		
		this.userSessionPersistence = new MemoryUserSessionPersistence();

		this.restTemplate = this.createRestTemplate();
		this.jsonMessageConverter = JsonMessageConverterSelector.getJsonMessageConverter();
		this.restTemplate.setMessageConverters(this.getMessageConverters());
		this.restTemplate.setErrorHandler(this.getResponseErrorHandler());
		this.restTemplate.setInterceptors(this.createInterceptors());
		
		this.downloadRestTemplate = this.createRestTemplate();
		
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
	
	/**
	 * @return the server URL the SDK is pointing to, by default is {@link AppGluTemplate#DEFAULT_BASE_URL}
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @return a randomly generated unique key specific for each mobile application
	 */
	public String getApplicationKey() {
		return applicationKey;
	}

	/**
	 * @return the secret key used to authenticate this application
	 */
	public String getApplicationSecret() {
		return applicationSecret;
	}
	
	/**
	 * @return environment name to be accessed, by default is {@link AppGluTemplate#DEFAULT_ENVIRONMENT}
	 */
	public String getApplicationEnvironment() {
		return applicationEnvironment;
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
	
	public StorageOperations storageOperations() {
		return storageOperations;
	}
	
	public AsyncStorageOperations asyncStorageOperations() {
		this.checkAsyncExecutor();
		return asyncStorageOperations;
	}

	public RestOperations restOperations() {
		return getRestTemplate();
	}
	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
	
	public RestOperations downloadRestOperations() {
		return getDownloadRestTemplate();
	}
	
	public RestTemplate getDownloadRestTemplate() {
		return downloadRestTemplate;
	}
	
	public AsyncExecutor getAsyncExecutor() {
		return asyncExecutor;
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
		this.storageOperations = new StorageTemplate(this.downloadRestOperations());
	}
	
	private void initAsyncApis() {
		this.asyncCrudOperations = new AsyncCrudTemplate(this.getAsyncExecutor(), this.crudOperations());
		this.asyncSavedQueriesOperations = new AsyncSavedQueriesTemplate(this.getAsyncExecutor(), this.savedQueriesOperations());
		this.asyncPushOperations = new AsyncPushTemplate(this.getAsyncExecutor(), this.pushOperations());
		this.asyncAnalyticsOperations = new AsyncAnalyticsTemplate(this.getAsyncExecutor(), this.analyticsOperations());
		this.asyncUserOperations = new AsyncUserTemplate(this.getAsyncExecutor(), this.userOperations());
		this.asyncSyncOperations = new AsyncSyncTemplate(this.getAsyncExecutor(), this.syncOperations());
		this.asyncStorageOperations = new AsyncStorageTemplate(this.getAsyncExecutor(), this.storageOperations());
	}
	
	protected RestTemplate createRestTemplate() {
		if (ANDROID_ENVIRONMENT) {
			return new RestTemplate();
		}
		return new RestTemplate(ClientHttpRequestFactorySelector.getRequestFactory());
	}

	protected void configureHttpRequestInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
		this.defaultHeadersHttpRequestInterceptor = new DefaultHeadersHttpRequestInterceptor(this.getBaseUrl(), this.getApplicationEnvironment());
		this.basicAuthHttpRequestInterceptor = new BasicAuthHttpRequestInterceptor(this.getApplicationKey(), this.getApplicationSecret());
		this.userSessionRequestInterceptor = new UserSessionRequestInterceptor(this.userSessionPersistence);
		
		interceptors.add(this.defaultHeadersHttpRequestInterceptor);
		interceptors.add(this.basicAuthHttpRequestInterceptor);
		interceptors.add(this.userSessionRequestInterceptor);
		
		if (ANDROID_ENVIRONMENT) {
			interceptors.add(new GZipHttpRequestInterceptor());
		}
	}

}