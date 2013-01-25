package com.appglu.android.sync;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import com.appglu.AppGluRestClientException;
import com.appglu.InputStreamCallback;
import com.appglu.SyncOperations;
import com.appglu.TableChanges;
import com.appglu.TableChangesCallback;
import com.appglu.TableVersion;
import com.appglu.impl.json.TableChangesBody;
import com.appglu.impl.json.TableChangesJsonParser;
import com.appglu.impl.json.TableVersionBody;
import com.appglu.impl.json.jackson.AppGluModule;
import com.appglu.impl.json.jackson.JacksonTableChangesJsonParser;
import com.appglu.impl.util.IOUtils;

public class MockSyncOperations implements SyncOperations {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private String changesForTablesJson;
	private String versionsForTablesJson;
	
	private TableChangesJsonParser tableChangesJsonParser;
	
	public MockSyncOperations(String changesForTablesJson, String versionsForTablesJson) {
		this.changesForTablesJson = changesForTablesJson;
		this.versionsForTablesJson = versionsForTablesJson;
		
		this.objectMapper.registerModule(new AppGluModule());
		this.tableChangesJsonParser = new JacksonTableChangesJsonParser(objectMapper);
	}

	public TableChanges changesForTable(String tableName, long version) throws AppGluRestClientException {
		throw new UnsupportedOperationException("Not implemented by this mock");
	}

	public List<TableChanges> changesForTables(TableVersion... tables) throws AppGluRestClientException {
		return this.changesForTables(Arrays.asList(tables));
	}

	public List<TableChanges> changesForTables(List<TableVersion> tables) throws AppGluRestClientException {
		if (changesForTablesJson == null) {
			return null;
		}

		InputStream inputStream = null;
		try {
			inputStream = this.jsonInputStream(changesForTablesJson);
			TableChangesBody body = objectMapper.readValue(inputStream, TableChangesBody.class);
			return body.getTables();
		} catch (IOException e) {
			throw new AppGluRestClientException(e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	public void changesForTables(TableChangesCallback tableChangesCallback, TableVersion... tables) throws AppGluRestClientException {
		this.changesForTables(Arrays.asList(tables), tableChangesCallback);
	}

	public void changesForTables(List<TableVersion> tables, final TableChangesCallback tableChangesCallback) throws AppGluRestClientException {
		this.downloadChangesForTables(tables, new InputStreamCallback() {
			
			public void doWithInputStream(InputStream inputStream) throws IOException {
				tableChangesJsonParser.parseTableChanges(inputStream, tableChangesCallback);
			}
		});
	}
	
	public void downloadChangesForTables(InputStreamCallback inputStreamCallback, TableVersion... tables) throws AppGluRestClientException {
		this.downloadChangesForTables(Arrays.asList(tables), inputStreamCallback);
	}

	public void downloadChangesForTables(List<TableVersion> tables, InputStreamCallback inputStreamCallback) throws AppGluRestClientException {
		if (changesForTablesJson == null) {
			return;
		}

		InputStream inputStream = null;
		try {
			inputStream = this.jsonInputStream(changesForTablesJson);
			inputStreamCallback.doWithInputStream(inputStream);
		} catch (IOException e) {
			throw new AppGluRestClientException(e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	public List<TableVersion> versionsForTables(String... tables) throws AppGluRestClientException {
		return this.versionsForTables(Arrays.asList(tables));
	}

	public List<TableVersion> versionsForTables(List<String> tables) throws AppGluRestClientException {
		if (versionsForTablesJson == null) {
			return null;
		}

		InputStream inputStream = null;
		try {
			inputStream = this.jsonInputStream(versionsForTablesJson);
			TableVersionBody body = objectMapper.readValue(inputStream, TableVersionBody.class);
			return body.getTables();
		} catch (IOException e) {
			throw new AppGluRestClientException(e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	protected InputStream jsonInputStream(String filename) throws IOException {
		ClassPathResource classPathResource = new ClassPathResource(filename + ".json", getClass());
		return classPathResource.getInputStream();
	}

	public void parseTableChanges(InputStream inputStream, TableChangesCallback tableChangesCallback) throws IOException {
		tableChangesJsonParser.parseTableChanges(inputStream, tableChangesCallback);
	}

}