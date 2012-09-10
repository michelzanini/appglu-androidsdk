package com.appglu.android.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.appglu.AsyncCallback;
import com.appglu.Rows;
import com.appglu.android.AppGlu;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        
        this.readAllQueries();
    }

	private void readAllQueries() {
		AppGlu.crudApi().readAllInBackground("appglu_queries", new AsyncCallback<Rows>() {
			
			private ProgressDialog progressDialog;
			
        	public void onPreExecute() {
        		this.progressDialog = ProgressDialog.show(MainActivity.this, "Loading", "Please wait...");
        	}
        	
			public void onResult(Rows rows) {
				Toast.makeText(MainActivity.this, rows.toString(), Toast.LENGTH_LONG).show();
			}
			
			public void onException(Exception exception) {
				Toast.makeText(MainActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			}
			
			public void onFinish() {
				this.progressDialog.dismiss();
			}
			
		});
	}
    
}