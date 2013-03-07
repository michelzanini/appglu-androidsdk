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

/**
 * Happens when an HTTP status error of the client series is received (anything between 400 and 499).
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class AppGluHttpClientException extends AppGluHttpStatusCodeException {
	
	public AppGluHttpClientException(int statusCode, Error error) {
		super(statusCode, error);
	}

}
