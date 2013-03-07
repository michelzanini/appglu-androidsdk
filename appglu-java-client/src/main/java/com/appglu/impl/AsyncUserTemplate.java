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
package com.appglu.impl;

import java.util.Map;
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

	/**
	 * {@inheritDoc}
	 */
	public void signupInBackground(final User user, AsyncCallback<AuthenticationResult> authenticationResultCallback) {
		asyncExecutor.execute(authenticationResultCallback, new Callable<AuthenticationResult>() {
			public AuthenticationResult call() {
				return userOperations.signup(user);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void loginInBackground(final String username, final String password, AsyncCallback<AuthenticationResult> authenticationResultCallback) {
		asyncExecutor.execute(authenticationResultCallback, new Callable<AuthenticationResult>() {
			public AuthenticationResult call() {
				return userOperations.login(username, password);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void refreshUserProfileInBackground(AsyncCallback<Void> refreshCallback) {
		asyncExecutor.execute(refreshCallback, new Callable<Void>() {
			public Void call() {
				userOperations.refreshUserProfile();
				return null;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void logoutInBackground(AsyncCallback<Boolean> logoutCallback) {
		asyncExecutor.execute(logoutCallback, new Callable<Boolean>() {
			public Boolean call() {
				return userOperations.logout();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void readDataInBackground(AsyncCallback<Map<String, Object>> readDataCallback) {
		asyncExecutor.execute(readDataCallback, new Callable<Map<String, Object>>() {
			public Map<String, Object> call() {
				return userOperations.readData();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeDataInBackground(final Map<String, Object> data, AsyncCallback<Void> writeDataCallback) {
		asyncExecutor.execute(writeDataCallback, new Callable<Void>() {
			public Void call() {
				userOperations.writeData(data);
				return null;
			}
		});
	}

}
