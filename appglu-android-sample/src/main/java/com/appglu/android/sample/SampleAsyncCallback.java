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
