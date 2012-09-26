package com.appglu.android.analytics.activity;

import com.appglu.android.AppGlu;

import android.app.ExpandableListActivity;

public class AppGluAnalyticsExpandableListActivity extends ExpandableListActivity {
	
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
