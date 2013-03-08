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

import java.text.NumberFormat;
import java.util.List;

import com.appglu.StorageFile;
import com.appglu.android.AppGlu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProductListAdapter extends ArrayAdapter<Product> {
	
	private LayoutInflater layoutInflater;
	private List<Product> products;

	public ProductListAdapter(Context context, List<Product> products) {
		super(context, R.layout.product_list_item, products);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.products = products;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			convertView = this.layoutInflater.inflate(R.layout.product_list_item, parent, false);
			
			holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
			holder.progress = (ProgressBar) convertView.findViewById(R.id.progress);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.description = (TextView) convertView.findViewById(R.id.description);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Product product = this.products.get(position);
		
		//load an image to an ImageView and also uses a ProgressBar while the image is being loaded
		AppGlu.syncApi().readBitmapToImageViewInBackground(new StorageFile(product.getImageName()), 100, 100, holder.thumbnail, holder.progress);
		
		holder.title.setText(product.getTitle());
		holder.description.setText(product.getBriefDescription());
		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String priceString = formatter.format(product.getPrice());
		holder.price.setText(priceString);
		
		return convertView;
	}
	
	static class ViewHolder {
		ImageView thumbnail;
		ProgressBar progress;
		TextView title;
		TextView description;
		TextView price;
	}
	
}