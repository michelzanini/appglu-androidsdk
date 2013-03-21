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

public class Product {
	
	private int id;
	
	private String title;
	
	private String briefDescription;
	
	private String longDescription;
	
	private Double price;
	
	private String buyUrl;
	
	private String imageName;
	
	private Integer categoryId;

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getBriefDescription() {
		return briefDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public Double getPrice() {
		return price;
	}

	public String getBuyUrl() {
		return buyUrl;
	}

	public String getImageName() {
		return imageName;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBriefDescription(String briefDescription) {
		this.briefDescription = briefDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setBuyUrl(String buyUrl) {
		this.buyUrl = buyUrl;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
}