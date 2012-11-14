package com.appglu.impl.test_objects;

import com.appglu.Column;

public class ColumnAnnotation {
	
	@Column
	public Integer age;
	
	@Column(columnName = "first_name")
	public String firstName;
	
	@Column(columnName = "last_name", required = true)
	public String lastName;
	
	@Column(required = true)
	public String description;

}