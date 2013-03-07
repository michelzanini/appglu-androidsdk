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
package com.appglu.android;

/**
 * It happens if a required configuration is missing and was not properly set by the time {@link AppGlu#initialize(android.content.Context, AppGluSettings)} was called.
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class AppGluNotProperlyConfiguredException extends RuntimeException {

	public AppGluNotProperlyConfiguredException(String msg) {
		super(msg);
	}

}
