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
 * <p>Represents the operation that will be executed on a single row of the SQLite database while synchronizing data.<br>
 * <p>If a row has changed on the server side, the sync operation will be {@link #UPDATE}.<br>
 * If a row is new on the server side, the sync operation will be {@link #INSERT}.<br>
 * If a row has being removed on the server side, the sync operation will be {@link #DELETE}.<br>
 * 
 * @see Row
 * @since 1.0.0
 */
public enum SyncOperation {
	
	INSERT,
	DELETE,
	UPDATE;

	public static SyncOperation getSyncOperation(String type) {
		for (SyncOperation operation : values()) {
			if (operation.toString().equalsIgnoreCase(type)) {
				return operation;
			}
		}
		return null;
	}
	
	public boolean isInsert() {
		return this == INSERT;
	}
	
	public boolean isDelete() {
		return this == DELETE;
	}
	
	public boolean isUpdate() {
		return this == UPDATE;
	}
	
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}
