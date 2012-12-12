package com.appglu.impl;

import static org.springframework.test.web.client.match.RequestMatchers.content;
import static org.springframework.test.web.client.match.RequestMatchers.header;
import static org.springframework.test.web.client.match.RequestMatchers.method;
import static org.springframework.test.web.client.match.RequestMatchers.requestTo;
import static org.springframework.test.web.client.response.ResponseCreators.withStatus;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.appglu.AppGluHttpClientException;
import com.appglu.ErrorCode;
import com.appglu.SyncOperation;
import com.appglu.SyncOperations;
import com.appglu.RowChanges;
import com.appglu.TableVersion;
import com.appglu.TableChanges;

public class SyncTemplateTest extends AbstractAppGluApiTest {
	
	private SyncOperations syncOperations;
	
	@Before
	public void setup() {
		super.setup();
		syncOperations = appGluTemplate.syncOperations();
	}
	
	@Test
	public void changesForTables() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/sync/changes"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/sync_changes_for_tables_request")))
			.andRespond(withStatus(HttpStatus.OK).body(compactedJson("data/sync_changes_for_tables_response")).headers(responseHeaders));
		
		TableVersion loggedTable = new TableVersion("logged_table");
		TableVersion otherTable = new TableVersion("other_table", 1);
		
		List<TableChanges> changes = this.syncOperations.changesForTables(loggedTable, otherTable);
		
		Assert.assertNotNull(changes);
		Assert.assertEquals(2, changes.size());
		
		TableChanges loggedTableChanges = changes.get(0);
		this.assertTable(loggedTableChanges, "logged_table", 9, 2);
		
		RowChanges firstRow = loggedTableChanges.getChanges().get(0);
		this.assertRow(firstRow, 2, 1, "row1", 1, SyncOperation.INSERT);
		
		RowChanges secondRow = loggedTableChanges.getChanges().get(1);
		this.assertRow(secondRow, 2, 2, "row2", 2, SyncOperation.UPDATE);
		
		TableChanges otherTableChanges = changes.get(1);
		this.assertTable(otherTableChanges, "other_table", 1, 1);
		
		RowChanges firstRowOtherTable = otherTableChanges.getChanges().get(0);
		this.assertRow(firstRowOtherTable, 2, 1, "row1", 6, SyncOperation.DELETE);
		
		mockServer.verify();
	}
	
	@Test
	public void changesForTable() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/sync/changes/logged_table?from_version=2"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.OK).body(compactedJson("data/sync_changes_for_table_response")).headers(responseHeaders));
		
		TableChanges loggedTableChanges = this.syncOperations.changesForTable("logged_table", 2);
		
		this.assertTable(loggedTableChanges, "logged_table", 9, 2);
		
		RowChanges firstRow = loggedTableChanges.getChanges().get(0);
		this.assertRow(firstRow, 2, 1, "row1", 1, SyncOperation.INSERT);
		
		RowChanges secondRow = loggedTableChanges.getChanges().get(1);
		this.assertRow(secondRow, 2, 2, "row2", 2, SyncOperation.UPDATE);
		
		mockServer.verify();
	}
	
	@Test
	public void versionsForTables() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/sync/versions/logged_table,other_table"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.OK).body(compactedJson("data/sync_versions_for_tables_response")).headers(responseHeaders));
		
		List<TableVersion> versions = this.syncOperations.versionsForTables("logged_table", "other_table");
		
		Assert.assertNotNull(versions);
		Assert.assertEquals(2, versions.size());
		
		TableVersion loggedTable = versions.get(0);
		
		Assert.assertNotNull(loggedTable);
		Assert.assertEquals("logged_table", loggedTable.getTableName());
		Assert.assertEquals(9, loggedTable.getVersion());
		
		TableVersion otherTable = versions.get(1);
		
		Assert.assertNotNull(otherTable);
		Assert.assertEquals("other_table", otherTable.getTableName());
		Assert.assertEquals(1, otherTable.getVersion());
		
		mockServer.verify();
	}
	
	@Test
	public void versionsForTablesEmptyTablesParameter() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/sync/versions/"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.BAD_REQUEST).body(compactedJson("data/error_table_not_found")).headers(responseHeaders));
		
		try {
			this.syncOperations.versionsForTables("");
			Assert.fail("Should have caused a exception");
		} catch (AppGluHttpClientException ex) {
			Assert.assertEquals(ErrorCode.TABLE_DOES_NOT_EXISTS, ex.getError().getCode());
			Assert.assertEquals("Table {table} was not found.", ex.getError().getMessage());
		}
		
		mockServer.verify();
	}

	private void assertTable(TableChanges changes, String name, int version, int changesSize) {
		Assert.assertNotNull(changes);
		Assert.assertEquals(name, changes.getTableName());
		Assert.assertEquals(version, changes.getVersion());
		Assert.assertEquals(changesSize, changes.getChanges().size());
	}

	private void assertRow(RowChanges row, int numberOfProperties, int id, String name, int appgluKey, SyncOperation operation) {
		Assert.assertNotNull(row);
		Assert.assertEquals(numberOfProperties, row.getRow().size());
		
		Assert.assertEquals(new Integer(id), row.getRow().getInt("id"));
		Assert.assertEquals(name, row.getRow().getString("name"));
		Assert.assertEquals(appgluKey, row.getAppgluKey());
		Assert.assertEquals(operation, row.getAppgluSyncOperation());
	}
	
}