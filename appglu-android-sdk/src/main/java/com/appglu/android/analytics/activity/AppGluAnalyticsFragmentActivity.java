package com.appglu.android.analytics.activity;

import com.appglu.android.AppGlu;

import android.support.v4.app.FragmentActivity;

public class AppGluAnalyticsFragmentActivity extends FragmentActivity {
	
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