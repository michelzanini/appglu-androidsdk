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

import java.io.Serializable;

public class Column implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String type;
	
	private boolean primaryKey;
	
	public Column() {

	}

	public Column(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public Column(String name, String type, boolean primaryKey) {
		this.name = name;
		this.type = type;
		this.primaryKey = primaryKey;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setName(String name) {
		if (name != null) {
			this.name = name.toLowerCase();
		} else {
			this.name = null;
		}
	}

	public void setType(String type) {
		if (type != null) {
			this.type = type.toLowerCase();
		} else {
			this.type = null;
		}
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Column other = (Column) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Column [name=" + name + ", type=" + type + ", primaryKey="
				+ primaryKey + "]";
	}

}
