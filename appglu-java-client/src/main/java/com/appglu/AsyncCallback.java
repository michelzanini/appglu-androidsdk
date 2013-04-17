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
 * Define methods to be called as a callback while doing an <strong>asynchronous</strong> operation.<br>
 * {@code AsyncCallback} is used in every <strong>asynchronous</strong> method of the SDK.
 * 
 * @see com.appglu.impl.AsyncExecutor
 * @since 1.0.0
 */
public abstract class AsyncCallback<Result> {
	
	public void onPreExecute() {
		
	}
	
	public abstract void onResult(Result result);
	
	public void onException(ExceptionWrapper exceptionWrapper) {
		
	}
	
	public void onNoInternetConnection() {
		
	}
	
	public void onFinish(boolean wasSuccessful) {
		
	}

}
