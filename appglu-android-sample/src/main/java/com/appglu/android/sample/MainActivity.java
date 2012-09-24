package com.appglu.android.sample;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;
import com.appglu.AsyncCallback;
import com.appglu.QueryResult;
import com.appglu.Rows;
import com.appglu.android.AppGlu;
import com.appglu.android.SavedQuery;
import com.appglu.android.analytics.AnalyticsSessionCallback;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

public class MainActivity extends Activity {
	
	private Logger logger = LoggerFactory.getLogger("AppGluSample");
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        
        AppGlu.crudApi().readAllInBackground("appglu_queries", this.rowsCallback);
        
        AppGlu.analyticsApi().setSessionCallback(new AnalyticsSessionCallback() {
			
			@Override
			public void onStartSession(AnalyticsSession session) {
				logger.info("onStartSession");
				session.addParameter("onStartSession", "onStartSession");
			}
			
			@Override
			public void beforeUploadSessions(List<AnalyticsSession> sessions) {
				logger.info("beforeUploadSessions");
			}
			
		});
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