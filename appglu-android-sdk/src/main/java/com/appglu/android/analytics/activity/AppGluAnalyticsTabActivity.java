package com.appglu.android.analytics.activity;

import android.app.TabActivity;

import com.appglu.android.AppGlu;

public class AppGluAnalyticsTabActivity extends TabActivity {
	
	@Override
    protected void onResume() {
    	super.onResume();
    	AppGlu.analyticsApi().onActivityResume(this);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	AppGlu.analyticsApi().onActivityPause(this);
    }

}