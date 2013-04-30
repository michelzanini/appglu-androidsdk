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
package com.appglu.android.task;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.ExceptionWrapper;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

/**
 * Extension of Android's Ice Cream Sandwich port of <code>AsyncTask</code> to add {@link #onException(Exception)} and {@link #onFinished(boolean)} callbacks.
 * 
 * @since 1.0.0
 */
public class AppGluAsyncTaskCompat<Result> extends AsyncTaskCompat<Void, Void, Result> {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private Exception exception;
	
	private AsyncCallback<Result> asyncCallback;
	
	private Callable<Result> workerThreadCallback;
	
	public AppGluAsyncTaskCompat(AsyncCallback<Result> asyncCallback, Callable<Result> workerThreadCallback) {
		this.asyncCallback = asyncCallback;
		this.workerThreadCallback = workerThreadCallback;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		asyncCallback.onPreExecute();
	}

	@Override
	protected final Result doInBackground(Void... params) {
		try {
			return this.doExecuteInBackground(params);
		} catch (Exception e) {
			this.exception = e;
			return null;
		}
	}

	protected Result doExecuteInBackground(Void... params) throws Exception {
		return workerThreadCallback.call();
	}

	@Override
	protected final void onPostExecute(Result result) {
		boolean wasSuccessful = this.exception == null;
		try {
			if (wasSuccessful) {
				this.onResult(result);
			} else {
				this.onException(exception);
			}
		} finally {
			this.onFinished(wasSuccessful);
		}
	}

	protected void onException(Exception exception) {
		logger.error(exception);
		asyncCallback.onException(new ExceptionWrapper(exception));
	}
	
	protected void onResult(Result result) {
		asyncCallback.onResult(result);
	}
	
	@Override
	protected void onCancelled() {
		this.onFinished(false);
	}
	
	protected void onFinished(boolean wasSuccessful) {
		asyncCallback.onFinish(wasSuccessful);
	}

}
