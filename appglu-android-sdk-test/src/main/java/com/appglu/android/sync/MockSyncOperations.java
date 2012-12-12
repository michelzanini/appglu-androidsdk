package com.appglu.android.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.appglu.AppGluRestClientException;
import com.appglu.SyncOperations;
import com.appglu.TableChanges;
import com.appglu.TableVersion;
import com.appglu.impl.json.AppGluModule;
import com.appglu.impl.json.TableChangesBody;
import com.appglu.impl.json.TableVersionBody;

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

		try {
			String json = this.compactedJson(changesForTablesJson);
			TableChangesBody body = objectMapper.readValue(json, TableChangesBody.class);
			return body.getTables();
		} catch (IOException e) {
			throw new AppGluRestClientException(e);
		}
	}

	public TableChanges changesForTable(String tableName, long version) throws AppGluRestClientException {
		return null;
	}

	public List<TableVersion> versionsForTables(String... tables) throws AppGluRestClientException {
		return this.versionsForTables(Arrays.asList(tables));
	}

	public List<TableVersion> versionsForTables(List<String> tables) throws AppGluRestClientException {
		if (versionsForTablesJson == null) {
			return null;
		}

		try {
			String json = this.compactedJson(versionsForTablesJson);
			TableVersionBody body = objectMapper.readValue(json, TableVersionBody.class);
			return body.getTables();
		} catch (IOException e) {
			throw new AppGluRestClientException(e);
		}
	}
	
	protected Resource jsonResource(String filename) {
		return new ClassPathResource(filename + ".json", getClass());
	}

	protected String compactedJson(String jsonFilename) {
		return readCompactedJsonResource(jsonResource(jsonFilename));
	}
	
	protected String readCompactedJsonResource(Resource resource) {
		StringBuilder resourceText = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			while (reader.ready()) {
				resourceText.append(reader.readLine().trim().replace("\n", ""));
			}
		} catch (IOException e) {
		}		
		return resourceText.toString();
	}

}