package com.appglu.android.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.appglu.AsyncCallback;
import com.appglu.ExceptionWrapper;

public abstract class SampleAsyncCallback<Result> extends AsyncCallback<Result> {
	
	private Activity activity;
	
	private ProgressDialog progressDialog;
	
	public SampleAsyncCallback(Activity activity) {
		this.activity = activity;
	}
	
	public void onPreExecute() {
		this.progressDialog = ProgressDialog.show(this.activity, "Loading", "Please wait...");
	}
	

	public void onException(ExceptionWrapper exceptionWrapper) {
		Toast.makeText(this.activity, exceptionWrapper.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
	}
	
	public void onNoInternetConnection() {
		Toast.makeText(this.activity, "No Internet Connection", Toast.LENGTH_LONG).show();
	}

	public void onFinish() {
		this.progressDialog.dismiss();
	}

}