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
package com.appglu.android;

import java.io.Serializable;

import com.appglu.impl.util.StringUtils;

/**
 * Result of validating the application version with the server side using {@link AppGlu#validateApplicationVersion()} method.<br>
 * If {@link #succeed()} is <code>false</code> then {@link #getAppUpdateUrl()} will contain an URL that can be displayed to the user, so he can update the app.<br>
 * You can configure the URL and the minimum version using AppGlu's dashboard website.
 * 
 * @see AppGlu#validateApplicationVersion()
 * @since 1.0.0
 */
public class VersionValidationResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean succeed;
	
	private String appUpdateUrl;

	public VersionValidationResult() {
		this.succeed = true;
	}

	public VersionValidationResult(String appUpdateUrl) {
		this.succeed = false;
		this.appUpdateUrl = appUpdateUrl;
	}

	public boolean succeed() {
		return succeed;
	}

	public String getAppUpdateUrl() {
		return appUpdateUrl;
	}
	
	public boolean hasAppUpdateUrl() {
		return StringUtils.isNotEmpty(appUpdateUrl);
	}

	public String toString() {
		return "ApplicationVersionValidationResult [succeed=" + succeed + ", appUpdateUrl=" + appUpdateUrl + "]";
	}
	
}