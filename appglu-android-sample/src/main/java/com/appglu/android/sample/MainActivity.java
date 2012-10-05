package com.appglu.android.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appglu.AnalyticsSessionEvent;
import com.appglu.AsyncCallback;
import com.appglu.ExceptionWrapper;
import com.appglu.QueryResult;
import com.appglu.Rows;
import com.appglu.android.AppGlu;
import com.appglu.android.SavedQuery;

public class MainActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        
        AppGlu.crudApi().readAllInBackground("appglu_queries", this.rowsCallback);
        
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SecondActivity.class);
				startActivity(intent);
			}
		});
    }
    
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
    
    protected void runQueryWithService() {
    	AppGlu.savedQueriesApi().runQueryInBackground("queryName1", this.queryResultCallback);
    }
    
    protected void runQueryWithObject() {
    	SavedQuery query = new SavedQuery("queryName1");
        query.runInBackground(this.queryResultCallback);
    }
    
    private AsyncCallback<Rows> rowsCallback = new AsyncCallback<Rows>() {
		
		private ProgressDialog progressDialog;
		
		public void onPreExecute() {
			this.progressDialog = ProgressDialog.show(MainActivity.this, "Loading", "Please wait...");
		}
    	
		public void onResult(Rows rows) {
			Toast.makeText(MainActivity.this, rows.toString(), Toast.LENGTH_SHORT).show();
			
			AnalyticsSessionEvent event = new AnalyticsSessionEvent();
			event.setName("rowsCallback");
			event.addParameter("totalRows", String.valueOf(rows.getTotalRows()));
			
			AppGlu.analyticsApi().logEvent(event);
		}
		
		public void onException(ExceptionWrapper exceptionWrapper) {
			Toast.makeText(MainActivity.this, exceptionWrapper.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		
		public void onNoInternetConnection() {
			Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
		}
		
		public void onFinish() {
			this.progressDialog.dismiss();
			runQueryWithService();
		}
		
	};
	
	private AsyncCallback<QueryResult> queryResultCallback = new AsyncCallback<QueryResult>() {
		
		private ProgressDialog progressDialog;
		
		public void onPreExecute() {
			this.progressDialog = ProgressDialog.show(MainActivity.this, "Loading", "Please wait...");
		}
    	
		public void onResult(QueryResult queryResult) {
			Toast.makeText(MainActivity.this, queryResult.toString(), Toast.LENGTH_SHORT).show();
			
			AnalyticsSessionEvent event = new AnalyticsSessionEvent();
			event.setName("queryResultCallback");
			event.addParameter("totalRows", String.valueOf(queryResult.getRows().size()));
			
			AppGlu.analyticsApi().logEvent(event);
		}
		
		public void onException(ExceptionWrapper exceptionWrapper) {
			Toast.makeText(MainActivity.this, exceptionWrapper.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		
		public void onNoInternetConnection() {
			Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
		}
		
		public void onFinish() {
			this.progressDialog.dismiss();
		}
		
	};
	
}