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
 * This callback will receive events while a JSON input stream is being parsed.<br>
 * This JSON is the response of the Sync Changes API, containing all rows changed since the last time sync executed.
 * 
 * @since 1.0.0
 */
public interface TableChangesCallback {
	
	/**
	 * Executed for each table parsed from the JSON input stream
	 * @param tableVersion the table name and version
	 * @param hasChanges if false doWithRowChanges will not be called
	 * @return if false doWithRowChanges will not be called
	 */
	boolean doWithTableVersion(TableVersion tableVersion, boolean hasChanges);
	
	/**
	 * Executed for each row parsed from the JSON input stream
	 * @param tableVersion the table that this row belongs to
	 * @param rowChanges contains the Row and the sync operation to be applied (delete, insert or update)
	 */
	void doWithRowChanges(TableVersion tableVersion, RowChanges rowChanges);
	
}
