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

import org.springframework.http.HttpStatus;

/**
 * Happens when {@link UserOperations#login(String, String)} does not succeed because either the login or the password does not match.<br>
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class AppGluHttpUserUnauthorizedException extends AppGluHttpClientException {
	
	public AppGluHttpUserUnauthorizedException(Error error) {
		super(HttpStatus.UNAUTHORIZED.value(), error);
	}

}
