package com.appglu.android.analytics.activity;

import com.appglu.android.AppGlu;

import android.app.ListActivity;

public class AppGluAnalyticsListActivity extends ListActivity {
	
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