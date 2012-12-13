package com.appglu.android.sync.sqlite;

import java.io.Serializable;

public class Column implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String type;
	
	private boolean nullable;
	
	private boolean primaryKey;

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public boolean isNullable() {
		return nullable;
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

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
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
		return "ColumnDescription [name=" + name + ", type=" + type
				+ ", nullable=" + nullable + ", primaryKey=" + primaryKey + "]";
	}

}