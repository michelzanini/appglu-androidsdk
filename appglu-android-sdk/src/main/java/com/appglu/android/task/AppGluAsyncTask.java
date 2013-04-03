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

import android.os.AsyncTask;

/**
 * Extension of Android's <code>AsyncTask</code> to add {@link #onException(Exception)} and {@link #onFinished()} callbacks.
 * 
 * @since 1.0.0
 */
public abstract class AppGluAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	
	private Exception exception;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected final Result doInBackground(Params... params) {
		try {
			return this.doExecuteInBackground(params);
		} catch (Exception e) {
			this.exception = e;
			return null;
		}
	}

	protected abstract Result doExecuteInBackground(Params... params) throws Exception;

	@Override
	protected final void onPostExecute(Result result) {
		try {
			if (this.exception != null) {
				this.onException(exception);
			} else {
				this.onResult(result);
			}
		} finally {
			this.onFinished();
		}
	}

	protected void onException(Exception exception) {
		
	}
	
	protected abstract void onResult(Result result);
	
	@Override
	protected void onCancelled() {
		this.onFinished();
	}
	
	protected void onFinished() {
		
	}
	
	public boolean isRunning() {
		return this.getStatus() == Status.RUNNING;
	}
	
	public boolean isPending() {
		return this.getStatus() == Status.PENDING;
	}
	
	public boolean isFinished() {
		return this.getStatus() == Status.FINISHED;
	}

}
