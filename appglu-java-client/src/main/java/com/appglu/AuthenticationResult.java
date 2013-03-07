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

import java.io.Serializable;

/**
 * Contains the result of a mobile application user sign up or log in.<br>
 * If <code>succeed</code> is <code>false</code> then <code>error</code> will be not <code>null</code>.
 * 
 * @see UserOperations
 * @since 1.0.0
 */
public class AuthenticationResult implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private boolean succeed;
	
	private Error error;
	
	public AuthenticationResult(boolean succeed) {
		this.succeed = succeed;
	}
	
	public AuthenticationResult(boolean succeed, Error error) {
		this.succeed = succeed;
		this.error = error;
	}

	public boolean succeed() {
		return succeed;
	}
	
	public Error getError() {
		return error;
	}
	
	public boolean hasError() {
		return error != null;
	}

	public String toString() {
		return "AuthenticationResult [succeed=" + succeed + ", error=" + error + "]";
	}

}
