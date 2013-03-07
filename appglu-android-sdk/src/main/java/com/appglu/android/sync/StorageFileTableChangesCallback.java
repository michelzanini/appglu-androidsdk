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
package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.List;

import com.appglu.Row;
import com.appglu.RowChanges;
import com.appglu.StorageFile;
import com.appglu.StorageFileRowMapper;
import com.appglu.TableChangesCallback;
import com.appglu.TableVersion;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

public class StorageFileTableChangesCallback implements TableChangesCallback {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private StorageFileRowMapper rowMapper = new StorageFileRowMapper();
	
	private List<StorageFile> parsedFiles = new ArrayList<StorageFile>();
	
	public boolean doWithTableVersion(TableVersion tableVersion, boolean hasChanges) {
		return SyncDatabaseHelper.APPGLU_STORAGE_FILES_TABLE.equals(tableVersion.getTableName());
	}
	
	public void doWithRowChanges(TableVersion tableVersion, RowChanges rowChanges) {
		Row row = rowChanges.getRow();
		
		if (row.isEmpty()) {
			logger.warn("Ignoring the parsing of files because row changes is empty");
			return;
		}
		
		if (rowChanges.getSyncOperation() != null && !rowChanges.getSyncOperation().isDelete()) {
			parsedFiles.add(rowMapper.mapRow(rowChanges.getRow()));
		}
	}
	
	public List<StorageFile> getParsedFiles() {
		return parsedFiles;
	}

}
