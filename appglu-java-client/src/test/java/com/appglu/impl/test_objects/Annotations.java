package com.appglu.impl.test_objects;

import com.appglu.Column;
import com.appglu.Ignore;
import com.appglu.Table;

@Table (tableName = "OBJECT_ANNOTATIONS")
public class Annotations {
	
	@Ignore
	public Integer age;
	
	@Column (columnName = "fName")
	public String firstName;
	
	@Column (columnName = "lName", required = true)
	public String lastName;
	
	@Column (required = true)
	public String description;

}