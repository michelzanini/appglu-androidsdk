package com.appglu.android.sample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				AppGluTestRestClient restClient = new AppGluTestRestClient();
				restClient.callApiEndpoints();
				return null;
			}
			
		}.execute();
		
    }
    
}

