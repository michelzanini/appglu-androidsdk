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

import android.app.Application;

import com.appglu.android.AppGlu;
import com.appglu.android.AppGluSettings;
import com.appglu.android.log.LoggerLevel;

public class SampleApplication extends Application {
	
	public static final String LOG_TAG = "AppGluSample";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		AppGluSettings settings = new AppGluSettings("ENTER_APP_KEY", "ENTER_APP_SECRET");
		
		settings.setDefaultSyncDatabaseHelper(new ProductsDatabaseHelper(this));
		settings.setLoggerLevel(LoggerLevel.DEBUG);
		
		AppGlu.initialize(this, settings);
	}
	
}
