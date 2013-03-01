package com.appglu.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.appglu.AnalyticsOperations;
import com.appglu.AnalyticsSession;
import com.appglu.AsyncAnalyticsOperations;
import com.appglu.AsyncCallback;

public final class AsyncAnalyticsTemplate implements AsyncAnalyticsOperations {
	
	private final AsyncExecutor asyncExecutor;
	
	private final AnalyticsOperations analyticsOperations;
	
	public AsyncAnalyticsTemplate(AsyncExecutor asyncExecutor, AnalyticsOperations analyticsOperations) {
		this.asyncExecutor = asyncExecutor;
		this.analyticsOperations = analyticsOperations;
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadSessionInBackground(final AnalyticsSession session, AsyncCallback<Void> callback) {
		asyncExecutor.execute(callback, new Callable<Void>() {
			public Void call() {
				analyticsOperations.uploadSession(session);
				return null;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadSessionsInBackground(final List<AnalyticsSession> sessions, AsyncCallback<Void> callback) {
		asyncExecutor.execute(callback, new Callable<Void>() {
			public Void call() {
				analyticsOperations.uploadSessions(sessions);
				return null;
			}
		});
	}
	
	public void uploadSessionsInBackground(AsyncCallback<Void> callback, AnalyticsSession... sessions) {
		this.uploadSessionsInBackground(Arrays.asList(sessions), callback);
	}

}