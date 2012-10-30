package com.appglu.impl;

import static org.springframework.test.web.client.match.RequestMatchers.content;
import static org.springframework.test.web.client.match.RequestMatchers.header;
import static org.springframework.test.web.client.match.RequestMatchers.method;
import static org.springframework.test.web.client.match.RequestMatchers.requestTo;
import static org.springframework.test.web.client.response.ResponseCreators.withStatus;
import static org.springframework.test.web.client.response.ResponseCreators.withSuccess;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.appglu.CrudOperations;
import com.appglu.ReadAllFilterArguments;
import com.appglu.Row;
import com.appglu.Rows;
import com.appglu.SortDirection;
import com.appglu.impl.util.DateUtils;

public class CrudTemplateTest extends AbstractAppGluApiTest {
	
	private CrudOperations crudOperations;
	
	@Before
	public void setup() {
		super.setup();
		crudOperations = appGluTemplate.crudOperations();
	}
	
	private Row row() {
		Row row = new Row();
		
		row.put("name", "John Due");
		row.put("enabled", true);
		row.put("login", "john");
		row.put("password", "due");
		row.put("age", 26);
		
		Row manyToOne = new Row();
		manyToOne.put("id", 1);
		row.put("many_to_one", manyToOne);
		
		List<Row> manyToMany = new ArrayList<Row>();
		
		Row one = new Row();
		one.put("id", 2);
		manyToMany.add(one);
		
		Row two = new Row();
		two.put("id", 1);
		manyToMany.add(two);
		
		row.put("many_to_many", manyToMany);
		return row;
	}
	
	private void assertRow(Row row) {
		Assert.assertEquals("John Due", row.get("name"));
		Assert.assertEquals(true, row.get("enabled"));
		Assert.assertEquals("john", row.get("login"));
		Assert.assertEquals("due", row.get("password"));
		Assert.assertEquals(26, row.get("age"));
	}
	
	private void assertRowWithRelationships(Row row) {
		assertRow(row);
		
		Row manyToOne = row.getRow("many_to_one");
		Assert.assertEquals(1, manyToOne.get("id"));
		
		List<Row> manyToMany = row.getRows("many_to_many");
		Assert.assertEquals(2, manyToMany.get(0).get("id"));
		Assert.assertEquals(1, manyToMany.get(1).get("id"));
	}
	
	@Test
	public void create() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/crud_row_relationships")))
			.andRespond(withStatus(HttpStatus.CREATED).body(compactedJson("data/crud_create_response")).headers(responseHeaders));
		
		Row row = row();
		
		Object id = crudOperations.create("user", row);
		Assert.assertEquals(8, id);
		
		mockServer.verify();
	}

	@Test
	public void update() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user/1"))
			.andExpect(method(HttpMethod.PUT))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/crud_row_relationships")))
			.andRespond(withSuccess().headers(responseHeaders));
		
		Row row = row();
		
		boolean success = crudOperations.update("user", 1, row);
		Assert.assertTrue(success);
		
		mockServer.verify();
	}
	
	@Test
	public void updateNotFound() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user/2"))
			.andExpect(method(HttpMethod.PUT))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/crud_row_relationships")))
			.andRespond(withStatus(HttpStatus.NOT_FOUND).body(compactedJson("data/error_not_found")).headers(responseHeaders));
		
		Row row = row();
		
		boolean success = crudOperations.update("user", 2, row);
		Assert.assertFalse(success);
		
		mockServer.verify();
	}

	@Test
	public void read() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user/1?expand_relationships=false"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body(compactedJson("data/crud_row")).headers(responseHeaders));
		
		Row row = crudOperations.read("user", 1);
		this.assertRow(row);
		
		mockServer.verify();
	}

	@Test
	public void readExpandRelationships() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user/1?expand_relationships=true"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body(compactedJson("data/crud_row_relationships")).headers(responseHeaders));
		
		Row row = crudOperations.read("user", 1, true);
		this.assertRowWithRelationships(row);
		
		mockServer.verify();
	}
	
	@Test
	public void readNotFound() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user/2?expand_relationships=false"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.NOT_FOUND).body(compactedJson("data/error_not_found")).headers(responseHeaders));
		
		Row row = crudOperations.read("user", 2);
		Assert.assertNull(row);
		
		mockServer.verify();
	}
	
	@Test
	public void readAll() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user?expand_relationships=false"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body(compactedJson("data/crud_rows")).headers(responseHeaders));
		
		Rows rows = crudOperations.readAll("user");
		
		List<Row> rowsList = rows.getRows();
		
		Assert.assertEquals(3, rowsList.size());
		Assert.assertEquals(new Integer(10), rows.getTotalRows());
		
		assertRow(rowsList.get(0));
		assertRow(rowsList.get(1));
		assertRow(rowsList.get(2));
		
		mockServer.verify();
	}
	
	@Test
	public void readAllEmpty() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user?expand_relationships=false"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body(compactedJson("data/crud_rows_empty")).headers(responseHeaders));
		
		Rows rows = crudOperations.readAll("user");
		
		List<Row> rowsList = rows.getRows();
		
		Assert.assertEquals(0, rowsList.size());
		Assert.assertEquals(new Integer(0), rows.getTotalRows());
		
		mockServer.verify();
	}
	
	@Test
	public void readAllExpandRelationships() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user?expand_relationships=true"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body(compactedJson("data/crud_rows_relationships")).headers(responseHeaders));
		
		Rows rows = crudOperations.readAll("user", true);
		
		List<Row> rowsList = rows.getRows();
		
		Assert.assertEquals(3, rowsList.size());
		Assert.assertEquals(new Integer(10), rows.getTotalRows());
		
		assertRowWithRelationships(rowsList.get(0));
		assertRowWithRelationships(rowsList.get(1));
		assertRowWithRelationships(rowsList.get(2));
		
		mockServer.verify();
	}
	
	@Test
	public void readAllLimitOffset() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user?limit=10&offset=100&expand_relationships=false"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body(compactedJson("data/crud_rows")).headers(responseHeaders));
		
		ReadAllFilterArguments arguments = new ReadAllFilterArguments(10, 100, null, null, null, null);
		crudOperations.readAll("user", false, arguments);
		
		mockServer.verify();
	}
	
	@Test
	public void readAllFiterArguments() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user?filter_column=name&filter_query=John%20Due&expand_relationships=false"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body(compactedJson("data/crud_rows")).headers(responseHeaders));
		
		ReadAllFilterArguments arguments = new ReadAllFilterArguments(0, 0, "name", "John Due", null, null);
		crudOperations.readAll("user", false, arguments);
		
		mockServer.verify();
	}
	
	@Test
	public void readAllSortArguments() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user?sort_column=name&sort_direction=ASC&expand_relationships=false"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body(compactedJson("data/crud_rows")).headers(responseHeaders));
		
		ReadAllFilterArguments arguments = new ReadAllFilterArguments(0, 0, null, null, "name", SortDirection.ASC);
		crudOperations.readAll("user", false, arguments);
		
		mockServer.verify();
	}
	
	@Test
	public void delete() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user/1"))
			.andExpect(method(HttpMethod.DELETE))
			.andRespond(withSuccess().headers(responseHeaders));
		
		boolean success = crudOperations.delete("user", 1);
		Assert.assertTrue(success);
		
		mockServer.verify();
	}
	
	@Test
	public void deleteNotFound() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/user/2"))
			.andExpect(method(HttpMethod.DELETE))
			.andRespond(withStatus(HttpStatus.NOT_FOUND).body(compactedJson("data/error_not_found")).headers(responseHeaders));
		
		boolean success = crudOperations.delete("user", 2);
		Assert.assertFalse(success);
		
		mockServer.verify();
	}
	
	@Test
	public void readAllDataTypes() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/data_types/1?expand_relationships=false"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body(compactedJson("data/crud_read_all_data_types")).headers(responseHeaders));
		
		Row row = crudOperations.read("data_types", 1);
		
		String string = new String("a very long string for test");
		
		Assert.assertEquals(new Boolean(true), row.getBoolean("boolean"));
		Assert.assertEquals(new Short((short) 1), row.getShort("short"));
		Assert.assertEquals(new Byte((byte) 2), row.getByte("byte"));
		Assert.assertEquals(string, new String(row.getByteArray("byteArray")));
		Assert.assertEquals(new Float(1.5f), row.getFloat("float"));
		Assert.assertEquals(new Double(7.5d), row.getDouble("double"));
		Assert.assertEquals(new Integer(10), row.getInt("integer"));
		Assert.assertEquals(new Long(21474836475L), row.getLong("long"));
		Assert.assertEquals(new BigInteger("9223372036854775807123"), row.getBigInteger("bigInteger"));
		Assert.assertEquals(string, row.getString("string"));
		
		Assert.assertEquals("2010-01-15T12:10:00+0000", DateUtils.formatDatetime(row.getDate("datetime")));
		Assert.assertEquals("1970-01-01T12:10:00+0000", DateUtils.formatDatetime(row.getDate("time")));
		Assert.assertEquals("2010-01-15T00:00:00+0000", DateUtils.formatDatetime(row.getDate("date")));
		
		mockServer.verify();
	}
	
	@Test
	public void writeAllDataTypes() throws ParseException {
		mockServer.expect(requestTo("http://localhost/appglu/v1/tables/data_types"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/crud_write_all_data_types")))
			.andRespond(withStatus(HttpStatus.CREATED).body(compactedJson("data/crud_create_response")).headers(responseHeaders));
		
		Row row = new Row();
		
		String string = new String("a very long string for test");
		
		row.put("boolean", true);
		row.put("short", new Short((short) 1));
		row.put("byte", new Byte((byte) 2));
		row.put("byteArray", string.getBytes());
		row.put("float", new Float(1.5f));
		row.put("double", new Double(7.5d));
		row.put("integer", new Integer(10));
		row.put("long", new Long(21474836475L));
		row.put("bigInteger", new BigInteger("9223372036854775807123"));
		row.put("string", string);
		
		Date datetime = DateUtils.parseDate("2010-01-15T12:10:00+0000");
		row.putDatetime("datetime", datetime);
		
		Date time = DateUtils.parseDate("1970-01-01T12:10:00+0000");
		row.putTime("time", time);
		
		Date date = DateUtils.parseDate("2010-01-15T00:00:00+0000");
		row.putDate("date", date);
		
		Object id = crudOperations.create("data_types", row);
		Assert.assertEquals(8, id);
		
		mockServer.verify();
	}

}