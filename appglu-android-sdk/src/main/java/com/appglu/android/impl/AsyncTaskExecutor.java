package com.appglu.android.impl;

import com.appglu.AsyncCallback;
import com.appglu.impl.AsyncExecutor;
import com.appglu.impl.AsyncExecutorCallback;

public class AsyncTaskExecutor implements AsyncExecutor {

	@Override
	public <Result> void execute(final AsyncCallback<Result> asyncCallback, final AsyncExecutorCallback<Result> executorCallback) {
		AppGluAsyncTask<Void, Void, Result> asyncTask = new AppGluAsyncTask<Void, Void, Result>() {
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				asyncCallback.onPreExecute();
			}

			@Override
			protected Result doExecuteInBackground(Void... params) throws Exception {
				return executorCallback.doExecute();
			}

			@Override
			protected void onResult(Result result) {
				asyncCallback.onResult(result);
			}

			@Override
			protected void onException(Exception exception) {
				super.onException(exception);
				asyncCallback.onException(exception);
			}

			@Override
			protected void onFinished() {
				super.onFinished();
				asyncCallback.onFinish();
			}
			
		};
		
		asyncTask.execute();
	}

}