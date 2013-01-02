package com.appglu.android.sync;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import com.appglu.AppGluRestClientException;
import com.appglu.SyncOperations;
import com.appglu.TableChanges;
import com.appglu.TableChangesCallback;
import com.appglu.TableVersion;
import com.appglu.impl.json.TableChangesBody;
import com.appglu.impl.json.TableVersionBody;
import com.appglu.impl.json.jackson.AppGluModule;
import com.appglu.impl.json.jackson.JacksonTableChangesJsonParser;
import com.appglu.impl.util.IOUtils;

public class MockSyncOperations implements SyncOperations {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private String changesForTablesJson;
	private String versionsForTablesJson;
	
	public MockSyncOperations(String changesForTablesJson, String versionsForTablesJson) {
		this.changesForTablesJson = changesForTablesJson;
		this.versionsForTablesJson = versionsForTablesJson;
		
		objectMapper.registerModule(new AppGluModule());
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

	public void changesForTables(List<TableVersion> tables, TableChangesCallback tableChangesCallback) throws AppGluRestClientException {
		if (changesForTablesJson == null) {
			return;
		}

		InputStream inputStream = null;
		try {
			inputStream = this.jsonInputStream(changesForTablesJson);
			JacksonTableChangesJsonParser parser = new JacksonTableChangesJsonParser(objectMapper);
			parser.parseTableChanges(inputStream, tableChangesCallback);
		} catch (IOException e) {
			throw new AppGluRestClientException(e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	public TableChanges changesForTable(String tableName, long version) throws AppGluRestClientException {
		throw new UnsupportedOperationException("Not implemented by this mock");
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

}