package com.appglu.android;

import java.util.concurrent.Callable;

import android.os.Handler;

import com.appglu.AsyncCallback;
import com.appglu.ExceptionWrapper;
import com.appglu.impl.AsyncExecutor;

public class AsyncTaskExecutor implements AsyncExecutor {
	
	private Handler handler = new Handler();

	@Override
	public <Result> void execute(final AsyncCallback<Result> asyncCallback, final Callable<Result> executorCallable) {
		AppGluAsyncTask<Void, Void, Result> asyncTask = new AppGluAsyncTask<Void, Void, Result>() {
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				asyncCallback.onPreExecute();
			}

			@Override
			protected Result doExecuteInBackground(Void... params) throws Exception {
				return executorCallable.call();
			}

			@Override
			protected void onResult(Result result) {
				asyncCallback.onResult(result);
			}

			@Override
			protected void onException(Exception exception) {
				super.onException(exception);
				asyncCallback.onException(new ExceptionWrapper(exception));
			}

			@Override
			protected void onFinished() {
				super.onFinished();
				asyncCallback.onFinish();
			}
			
		};
		
		if (AppGlu.hasInternetConnection()) {
			asyncTask.execute();	
		} else {
			handler.post(new Runnable() {
				public void run() {
					asyncCallback.onNoInternetConnection();
				}
			});
		}
	}

}