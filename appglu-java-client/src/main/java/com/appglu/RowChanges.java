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
 * {@code RowChanges} contains the {@link SyncOperation} to be applied for this {@link Row}.<br>
 * If the operation is {@link SyncOperation#INSERT} or {@link SyncOperation#UPDATE} then {@link #getRow()} will contain the data to be used for that insert or update.
 */
public class RowChanges {
	
	private Row row;
	
	private long syncKey;
	
	private SyncOperation syncOperation;

	public Row getRow() {
		if (row == null) {
			row = new Row();
		}
		return row;
	}

	public long getSyncKey() {
		return syncKey;
	}

	public SyncOperation getSyncOperation() {
		return syncOperation;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	public void setSyncKey(long appgluKey) {
		this.syncKey = appgluKey;
	}

	public void setSyncOperation(SyncOperation appgluSyncOperation) {
		this.syncOperation = appgluSyncOperation;
	}
	
	public void addRowProperty(String key, Object value) {
		getRow().put(key, value);
	}

	@Override
	public String toString() {
		return "RowChanges [syncKey=" + syncKey + ", syncOperation=" + syncOperation + "]";
	}

}
