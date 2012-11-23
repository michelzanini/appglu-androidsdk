package com.appglu.impl;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.AsyncUserOperations;
import com.appglu.AuthenticationResult;
import com.appglu.User;
import com.appglu.UserOperations;

public final class AsyncUserTemplate implements AsyncUserOperations {
	
	private final AsyncExecutor asyncExecutor;
	
	private final UserOperations userOperations;
	
	public AsyncUserTemplate(AsyncExecutor asyncExecutor, UserOperations userOperations) {
		this.asyncExecutor = asyncExecutor;
		this.userOperations = userOperations;
	}

	public void signupInBackground(final User user, AsyncCallback<AuthenticationResult> authenticationResultCallback) {
		asyncExecutor.execute(authenticationResultCallback, new Callable<AuthenticationResult>() {
			public AuthenticationResult call() {
				return userOperations.signup(user);
			}
		});
	}

	public void loginInBackground(final String username, final String password, AsyncCallback<AuthenticationResult> authenticationResultCallback) {
		asyncExecutor.execute(authenticationResultCallback, new Callable<AuthenticationResult>() {
			public AuthenticationResult call() {
				return userOperations.login(username, password);
			}
		});
	}

	public void refreshUserProfileInBackground(AsyncCallback<Void> refreshCallback) {
		asyncExecutor.execute(refreshCallback, new Callable<Void>() {
			public Void call() {
				userOperations.refreshUserProfile();
				return null;
			}
		});
	}

	public void logoutInBackground(AsyncCallback<Boolean> logoutCallback) {
		asyncExecutor.execute(logoutCallback, new Callable<Boolean>() {
			public Boolean call() {
				return userOperations.logout();
			}
		});
	}

}
