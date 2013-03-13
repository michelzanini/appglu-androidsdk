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
package com.appglu;

import java.io.Serializable;

import com.appglu.impl.util.StringUtils;

/**
 * An {@code Error} object has a code and a message. When the AppGlu REST API returns an error, it is parsed and represented with this class.
 * 
 * @see AppGluHttpStatusCodeException
 * @since 1.0.0
 */
public class Error implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private ErrorCode code;
	
	private String message;
	
	private String detail;
	
	public Error() {
		super();
	}
	
	public Error(ErrorCode code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public Error(ErrorCode code, String message, String detail) {
		this.code = code;
		this.message = message;
		this.detail = detail;
	}

	public ErrorCode getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public String getDetail() {
		return detail;
	}
	
	public boolean hasDetail() {
		return StringUtils.isNotEmpty(detail);
	}

	@Override
	public String toString() {
		if (this.hasDetail()) {
			return "Error [code=" + code + ", message=" + message + ", detail=" + detail + "]";
		}
		return "Error [code=" + code + ", message=" + message + "]";
	}
	
}
