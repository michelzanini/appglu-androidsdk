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
package com.appglu;

import java.util.Map;

/**
 * {@code AsyncUserOperations} has all methods that {@link UserOperations} has but they execute <strong>asynchronously</strong>. 
 * @see UserOperations
 * @since 1.0.0
 */
public interface AsyncUserOperations {
	
	/**
	 * Asynchronous version of {@link UserOperations#signup(User)}.
	 * @see UserOperations#signup(User)
	 */
	void signupInBackground(User user, AsyncCallback<AuthenticationResult> authenticationResultCallback);
	
	/**
	 * Asynchronous version of {@link UserOperations#login(String, String)}.
	 * @see UserOperations#login(String, String)
	 */
	void loginInBackground(String username, String password, AsyncCallback<AuthenticationResult> authenticationResultCallback);
	
	/**
	 * Asynchronous version of {@link UserOperations#refreshUserProfile()}.
	 * @see UserOperations#refreshUserProfile()
	 */
	void refreshUserProfileInBackground(AsyncCallback<Void> refreshCallback);
	
	/**
	 * Asynchronous version of {@link UserOperations#logout()}.
	 * @see UserOperations#logout()
	 */
	void logoutInBackground(AsyncCallback<Boolean> logoutCallback);
	
	/**
	 * Asynchronous version of {@link UserOperations#readData()}.
	 * @see UserOperations#readData()
	 */
	void readDataInBackground(AsyncCallback<Map<String, Object>> readDataCallback);
	
	/**
	 * Asynchronous version of {@link UserOperations#writeData(Map)}.
	 * @see UserOperations#writeData(Map)
	 */
	void writeDataInBackground(Map<String, Object> data, AsyncCallback<Void> writeDataCallback);

}
