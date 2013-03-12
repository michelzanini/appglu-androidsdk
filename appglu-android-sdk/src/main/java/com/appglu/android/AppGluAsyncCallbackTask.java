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
import com.appglu.ExceptionWrapper;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

/**
 * Adapt {@link com.appglu.android.AppGluAsyncTask} callbacks to a more user friendly version implemented in the {@link com.appglu.AsyncCallback} abstract class.<br>
 * {@link com.appglu.AsyncCallback} is used in every <strong>asynchronous</strong> method of the SDK.
 *  
 * @see com.appglu.impl.AsyncExecutor
 * @see com.appglu.AsyncCallback
 * @since 1.0.0
 */
public class AppGluAsyncCallbackTask<Result> extends AppGluAsyncTask<Void, Void, Result> {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private AsyncCallback<Result> asyncCallback;
	
	private Callable<Result> workerThreadCallback;
	
	public AppGluAsyncCallbackTask(AsyncCallback<Result> asyncCallback, Callable<Result> workerThreadCallback) {
		this.asyncCallback = asyncCallback;
		this.workerThreadCallback = workerThreadCallback;
	}

	protected void onPreExecute() {
		super.onPreExecute();
		asyncCallback.onPreExecute();
	}

	protected Result doExecuteInBackground(Void... params) throws Exception {
		return workerThreadCallback.call();
	}

	protected void onResult(Result result) {
		asyncCallback.onResult(result);
	}

	protected void onException(Exception exception) {
		super.onException(exception);
		logger.error(exception);
		asyncCallback.onException(new ExceptionWrapper(exception));
	}

	protected void onFinished() {
		super.onFinished();
		asyncCallback.onFinish();
	}

}
