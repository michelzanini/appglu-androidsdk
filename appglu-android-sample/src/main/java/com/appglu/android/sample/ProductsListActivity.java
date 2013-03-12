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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appglu.android.AppGlu;
import com.appglu.android.AppGluAsyncTask;
import com.appglu.android.analytics.activity.AppGluAnalyticsListActivity;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.sync.SyncBroadcastReceiver;
import com.appglu.android.sync.SyncExceptionWrapper;
import com.appglu.android.sync.SyncIntentFilter;
import com.appglu.android.sync.SyncIntentServiceRequest;

/**
 * Displays a list of {@link Product} objects in a ListView.<br>
 * Uses AppGlu's SyncApi to keep the list updated with the server.<br>
 */
public class ProductsListActivity extends AppGluAnalyticsListActivity {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private RelativeLayout loadingLayout;
	
	private SQLiteProductRepository productRepository;
	
	private BroadcastReceiver syncBroacastReceiver = new SyncBroadcastReceiver() {

		public void onPreExecute() {
			showLoadingView(true);
		};
		
		public void onResult(boolean changesWereApplied) {
			logger.info("Were any changes applied? " + changesWereApplied + ".");
		}

		public void onException(SyncExceptionWrapper exceptionWrapper) {
			String text = "An exception of type " + exceptionWrapper.getErrorCode() + " occured while executing the synchronization";
			Toast.makeText(ProductsListActivity.this, text, Toast.LENGTH_LONG).show();
			
			logger.error(exceptionWrapper.getException());
		}
		
		public void onNoInternetConnection() {
			onFinish();
		};
		
		/**
		 * Load local data no matter the sync was successful or not
		 */
		public void onFinish() {
			LoadProductsAsyncTask task = new LoadProductsAsyncTask();
			task.execute();
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.products_list_activity);
		
		this.loadingLayout = (RelativeLayout) this.findViewById(R.id.loading_layout);
		this.showLoadingView(true);
		
		ProductsDatabaseHelper databaseHelper = new ProductsDatabaseHelper(this);
		this.productRepository = new SQLiteProductRepository(databaseHelper);
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				productSelected(parent, position);
			}

		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.registerReceiver(this.syncBroacastReceiver, new SyncIntentFilter());
		
		this.synchronizeWithAppGlu();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(this.syncBroacastReceiver);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.products_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.refresh) {
			this.synchronizeWithAppGlu();	
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	private void productSelected(AdapterView<?> parent, int position) {
		Product product = (Product) parent.getItemAtPosition(position);
		
		Map<String, String> parameters = new HashMap<String, String>();
		
		parameters.put("item.id", String.valueOf(product.getId()));
		parameters.put("item.price", String.valueOf(product.getPrice()));
		
		AppGlu.analyticsApi().logEvent("product.clicked", parameters);
	}
	
	/**
	 * Will start the AppGlu Sync service to synchronize the local SQLite tables
	 */
	private void synchronizeWithAppGlu() {
		AppGlu.syncApi().startSyncIntentService(SyncIntentServiceRequest.syncDatabaseAndFiles());
	}
	
	private void showLoadingView(boolean show) {
		if (show) {
			loadingLayout.setVisibility(View.VISIBLE);
			getListView().setVisibility(View.GONE);
		} else {
			loadingLayout.setVisibility(View.GONE);
			getListView().setVisibility(View.VISIBLE);
		}
	}
	
	private void updateList(List<Product> products) {
		logger.info("Updating product list...");
		
		ProductListAdapter adapter = new ProductListAdapter(this, products);
		this.getListView().setAdapter(adapter);
	}
	
	private class LoadProductsAsyncTask extends AppGluAsyncTask<Void, Void, List<Product>> {
		
		@Override
		protected void onPreExecute() {
			showLoadingView(true);
		}

		@Override
		protected List<Product> doExecuteInBackground(Void... params) throws Exception {
			return productRepository.loadAllProducts();
		}

		@Override
		protected void onResult(List<Product> result) {
			updateList(result);
		}
		
		public void onException(Exception exception) {
			String text = "An exception occured while loading products from the database";
			Toast.makeText(ProductsListActivity.this, text, Toast.LENGTH_LONG).show();
			
			logger.error(exception);
		}
		
		@Override
		protected void onFinished() {
			showLoadingView(false);
		}
		
	}
	
}
