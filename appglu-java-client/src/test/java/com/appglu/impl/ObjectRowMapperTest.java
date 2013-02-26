package com.appglu.impl;

import org.junit.Assert;
import org.junit.Test;

import com.appglu.Row;
import com.appglu.RowMapper;
import com.appglu.RowMapperException;
import com.appglu.impl.test_objects.ColumnAnnotation;
import com.appglu.impl.test_objects.FieldTypeNotSupported;
import com.appglu.impl.test_objects.IgnoreAnnotation;
import com.appglu.impl.test_objects.InvalidFieldModifiers;
import com.appglu.impl.test_objects.IsAbstract;
import com.appglu.impl.test_objects.IsInterface;
import com.appglu.impl.test_objects.Subclass;
import com.appglu.impl.test_objects.WithWrongFieldType;
import com.appglu.impl.test_objects.WithoutEmptyConstructor;

public class ObjectRowMapperTest {
	
	@Test(expected = RowMapperException.class)
	public void objectIsAInterface() {
		RowMapper<IsInterface> rowMapper = new ObjectRowMapper<IsInterface>(IsInterface.class);
		rowMapper.mapRow(new Row());
	}
	
	@Test(expected = RowMapperException.class)
	public void objectWithoutEmptyConstructor() {
		RowMapper<WithoutEmptyConstructor> rowMapper = new ObjectRowMapper<WithoutEmptyConstructor>(WithoutEmptyConstructor.class);
		rowMapper.mapRow(new Row());
	}
	
	@Test(expected = RowMapperException.class)
	public void objectIsAbstract() {
		RowMapper<IsAbstract> rowMapper = new ObjectRowMapper<IsAbstract>(IsAbstract.class);
		rowMapper.mapRow(new Row());
	}
	
	@Test(expected = RowMapperException.class)
	public void objectWithWrongFieldType() {
		Row row = new Row();
		row.put("date", "string");
		
		RowMapper<WithWrongFieldType> rowMapper = new ObjectRowMapper<WithWrongFieldType>(WithWrongFieldType.class);
		rowMapper.mapRow(row);
	}
	
	@Test(expected = RowMapperException.class)
	public void fieldTypeNotSupported() {
		RowMapper<FieldTypeNotSupported> rowMapper = new ObjectRowMapper<FieldTypeNotSupported>(FieldTypeNotSupported.class);
		rowMapper.mapRow(new Row());
	}
	
	@Test
	public void propertiesOfSuperclassAreIgnored() {
		Row row = new Row();
		row.put("property_on_superclass", "property_on_superclass");
		row.put("property_on_subclass", "property_on_subclass");
		
		RowMapper<Subclass> rowMapper = new ObjectRowMapper<Subclass>(Subclass.class);
		Subclass subclass = rowMapper.mapRow(row);
		
		Assert.assertNull(subclass.propertyOnSuperclass);
		Assert.assertEquals("property_on_subclass", subclass.propertyOnSubclass);
	}
	
	@Test
	public void finalAndStaticFieldsAreIgnored() {
		Row row = new Row();
		
		row.put("static_field", "static_field");
		row.put("final_field", "final_field");
		row.put("normal_field", "normal_field");
		
		RowMapper<InvalidFieldModifiers> rowMapper = new ObjectRowMapper<InvalidFieldModifiers>(InvalidFieldModifiers.class);
		InvalidFieldModifiers invalidFields = rowMapper.mapRow(row);
		
		Assert.assertNull(InvalidFieldModifiers.staticField);
		Assert.assertEquals("", invalidFields.finalField);
		Assert.assertEquals("normal_field", invalidFields.normalField);
	}
	
	@Test
	public void annotationIgnoreWorks() {
		Row row = new Row();
		
		row.put("age", 18);
		row.put("name", "name");
		row.put("description", "description");
		
		RowMapper<IgnoreAnnotation> rowMapper = new ObjectRowMapper<IgnoreAnnotation>(IgnoreAnnotation.class);
		IgnoreAnnotation ignoreAnnotation = rowMapper.mapRow(row);
		
		Assert.assertNull(ignoreAnnotation.age);
		Assert.assertNull(ignoreAnnotation.name);
		Assert.assertEquals("description", ignoreAnnotation.description);
	}
	
	@Test
	public void columnAnnotationNameAttributeWorks() {
		Row row = new Row();
		
		row.put("age", 18);
		row.put("first_name", "first_name");
		row.put("last_name", "last_name");
		row.put("description", "description");
		
		RowMapper<ColumnAnnotation> rowMapper = new ObjectRowMapper<ColumnAnnotation>(ColumnAnnotation.class);
		ColumnAnnotation columnAnnotation = rowMapper.mapRow(row);
		
		Assert.assertEquals(new Integer(18), columnAnnotation.age);
		Assert.assertEquals("first_name", columnAnnotation.firstName);
		Assert.assertEquals("last_name", columnAnnotation.lastName);
		Assert.assertEquals("description", columnAnnotation.description);
	}
	
	@Test
	public void columnAnnotationRequiredAttributeWorks() {
		Row row = new Row();
		
		row.put("last_name", "last_name");
		row.put("description", "description");
		
		RowMapper<ColumnAnnotation> rowMapper = new ObjectRowMapper<ColumnAnnotation>(ColumnAnnotation.class);
		ColumnAnnotation columnAnnotation = rowMapper.mapRow(row);
		
		Assert.assertNull(columnAnnotation.age);
		Assert.assertNull(columnAnnotation.firstName);
		Assert.assertEquals("last_name", columnAnnotation.lastName);
		Assert.assertEquals("description", columnAnnotation.description);
		
		row = new Row();
		row.put("last_name", "last_name");
		
		this.failOnPropertyMissing(row, rowMapper);
		
		row = new Row();
		row.put("description", "description");
		
		this.failOnPropertyMissing(row, rowMapper);
	}

	private void failOnPropertyMissing(Row row, RowMapper<ColumnAnnotation> rowMapper) {
		try {
			rowMapper.mapRow(row);
			Assert.fail("@Column with required attribute will cause an exception if property not present");
		} catch (RowMapperException e) {
			
		}
	}

}
