package com.appglu;

public interface TableChangesCallback {
	
	void doWithTableVersion(TableVersion tableVersion, boolean hasChanges);
	
	void doWithRowChanges(TableVersion tableVersion, RowChanges rowChanges);
	
}