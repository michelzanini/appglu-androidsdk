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

import java.io.Serializable;
import java.util.List;

/**
 * Represents the result of a executed query.<br>
 * A select query will return a list of rows, use {@link #getRows()} to get it.<br>
 * A insert, update or delete query will return the quantity of rows affected, use {@link #getRowsAffected()} to get it.<br>
 * 
 * @see SavedQueriesOperations
 * @since 1.0.0
 */
public class QueryResult implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<Row> rows;
	
	private Integer rowsAffected;

	public List<Row> getRows() {
		return rows;
	}

	public Integer getRowsAffected() {
		return rowsAffected;
	}
	
	public boolean hasRows() {
		return rows != null && !rows.isEmpty();
	}
	
	public boolean hasRowsAffected() {
		return rowsAffected != null && rowsAffected > 0;
	}

	@Override
	public String toString() {
		return "QueryResult [rows=" + rows + ", rowsAffected=" + rowsAffected + "]";
	}

}
