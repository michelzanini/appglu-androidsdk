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

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.impl.AsyncExecutor;

/**
 * A {@link AsyncExecutor} implementation that uses {@link AppGluAsyncTask} to provide <strong>asynchronous</strong> execution.
 * 
 * @see com.appglu.android.AppGluAsyncTask
 * @see com.appglu.impl.AsyncExecutor
 * 
 * @since 1.0.0
 */
public class AsyncTaskExecutor implements AsyncExecutor {
	
	@Override
	public <Result> void execute(final AsyncCallback<Result> asyncCallback, final Callable<Result> workerThreadCallback) {
		AppGluAsyncCallbackTask<Result> asyncTask = new AppGluAsyncCallbackTask<Result>(asyncCallback, workerThreadCallback);
		
		if (AppGlu.hasInternetConnection()) {
			asyncTask.execute();
		} else {
			asyncCallback.onNoInternetConnection();
		}
	}

}
