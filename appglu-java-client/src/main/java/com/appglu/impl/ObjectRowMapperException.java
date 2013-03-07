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
package com.appglu.impl;

import com.appglu.RowMapperException;

/**
 * TODO
 * @since TODO
 */

@SuppressWarnings("serial")
public class ObjectRowMapperException extends RowMapperException {
	
	private Class<?> mappedClass;

	public ObjectRowMapperException(Class<?> mappedClass, String msg) {
		super(msg);
		this.mappedClass = mappedClass;
	}

	public ObjectRowMapperException(Class<?> mappedClass, Throwable e) {
		super(e);
		this.mappedClass = mappedClass;
	}

	public ObjectRowMapperException(Class<?> mappedClass, String msg, Exception e) {
		super(msg, e);
		this.mappedClass = mappedClass;
	}

	public Class<?> getMappedClass() {
		return mappedClass;
	}

	@Override
	public String getMessage() {
		if (mappedClass == null) {
			return super.getMessage();
		}
		return mappedClass.getName() + " - " + super.getMessage();
	}

}
