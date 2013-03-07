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

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all the delta changes of a particular table. Changes are new, updated or deleted rows since the last time this table was synchronized.
 * 
 * @since 1.0.0
 */
public class TableChanges {
	
	private String tableName;
	
	private long version;
	
	private List<RowChanges> changes = new ArrayList<RowChanges>();
	
	public TableChanges() {
		
	}
	
	public TableChanges(String tableName) {
		this.tableName = tableName;
	}

	public TableChanges(String tableName, long version) {
		this.tableName = tableName;
		this.version = version;
	}

	public String getTableName() {
		return tableName;
	}

	public long getVersion() {
		return version;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	public List<RowChanges> getChanges() {
		return changes;
	}
	
	public void setChanges(List<RowChanges> changes) {
		this.changes = changes;
	}
	
	public boolean hasChanges() {
		return !this.changes.isEmpty();
	}

	@Override
	public String toString() {
		return "[tableName=" + tableName + ", version=" + version + "]";
	}
	
}
