package com.appglu.impl.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.appglu.RowChanges;
import com.appglu.TableChanges;
import com.appglu.TableChangesCallback;
import com.appglu.TableVersion;

public class MemoryTableChangesCallback implements TableChangesCallback {
	
	private MultiValueMap<TableVersion, RowChanges> rowChangesByTable = new LinkedMultiValueMap<TableVersion, RowChanges>();

	public List<TableChanges> getTableChanges() {
		List<TableChanges> tableChanges = new ArrayList<TableChanges>();
		
		for (Entry<TableVersion, List<RowChanges>> entry : rowChangesByTable.entrySet()) {
			TableChanges changes = new TableChanges();
			
			changes.setTableName(entry.getKey().getTableName());
			changes.setVersion(entry.getKey().getVersion());
			changes.setChanges(entry.getValue());
			
			tableChanges.add(changes);
		}
		
		return tableChanges;
	}

	public void doWithTableVersion(TableVersion tableVersion, boolean hasChanges) {
		rowChangesByTable.put(tableVersion, new ArrayList<RowChanges>());
	}

	public void doWithRowChanges(TableVersion tableVersion, RowChanges rowChanges) {
		List<RowChanges> changes = rowChangesByTable.get(tableVersion);
		changes.add(rowChanges);
	}

}