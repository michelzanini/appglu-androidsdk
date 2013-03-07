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

/**
 * Happens when trying to convert a column value of a {@link Row} class to a incorrect type.<br>
 * For example, if the column type is <code>String</code> and you try to convert it to <code>Integer</code> using {@link Row#getInt(String)}.
 * 
 * @see Row
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class RowMapperTypeConversionException extends RowMapperException {
	
	public RowMapperTypeConversionException(String msg) {
		super(msg);
	}
	
	public RowMapperTypeConversionException(Exception e) {
		super(e);
	}
	
	public RowMapperTypeConversionException(String msg, Exception e) {
		super(msg, e);
	}
	
}
