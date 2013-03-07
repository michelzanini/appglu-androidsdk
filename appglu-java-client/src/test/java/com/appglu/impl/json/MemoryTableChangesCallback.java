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

	public boolean doWithTableVersion(TableVersion tableVersion, boolean hasChanges) {
		rowChangesByTable.put(tableVersion, new ArrayList<RowChanges>());
		return true;
	}

	public void doWithRowChanges(TableVersion tableVersion, RowChanges rowChanges) {
		List<RowChanges> changes = rowChangesByTable.get(tableVersion);
		changes.add(rowChanges);
	}

}
