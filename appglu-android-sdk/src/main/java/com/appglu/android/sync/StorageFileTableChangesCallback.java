package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.List;

import com.appglu.RowChanges;
import com.appglu.StorageFile;
import com.appglu.StorageFileRowMapper;
import com.appglu.TableChangesCallback;
import com.appglu.TableVersion;

public class StorageFileTableChangesCallback implements TableChangesCallback {
	
	private StorageFileRowMapper rowMapper = new StorageFileRowMapper();
	
	private List<StorageFile> parsedFiles = new ArrayList<StorageFile>();
	
	public boolean doWithTableVersion(TableVersion tableVersion, boolean hasChanges) {
		return SyncDatabaseHelper.APPGLU_STORAGE_FILES_TABLE.equals(tableVersion.getTableName());
	}
	
	public void doWithRowChanges(TableVersion tableVersion, RowChanges rowChanges) {
		if (!rowChanges.getSyncOperation().isDelete()) {
			parsedFiles.add(rowMapper.mapRow(rowChanges.getRow()));
		}
	}
	
	public List<StorageFile> getParsedFiles() {
		return parsedFiles;
	}

}