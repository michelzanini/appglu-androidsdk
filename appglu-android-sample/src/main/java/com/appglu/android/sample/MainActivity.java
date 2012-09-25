package com.appglu.android.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.appglu.AnalyticsSessionEvent;
import com.appglu.AsyncCallback;
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
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	AppGlu.analyticsApi().onActivityStart(this);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	AppGlu.analyticsApi().onActivityStop(this);
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
			
			AppGlu.analyticsApi().logEvent("rowsCallback");
		}
		
		public void onException(Exception exception) {
			Toast.makeText(MainActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
		
		public void onException(Exception exception) {
			Toast.makeText(MainActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		
		public void onNoInternetConnection() {
			Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
		}
		
		public void onFinish() {
			this.progressDialog.dismiss();
		}
		
	};
	
}