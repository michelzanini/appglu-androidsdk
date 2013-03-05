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