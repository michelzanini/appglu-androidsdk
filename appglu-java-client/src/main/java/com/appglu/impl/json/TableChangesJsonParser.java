package com.appglu.impl.json;

import java.io.IOException;
import java.io.InputStream;

import com.appglu.TableChangesCallback;

/**
 * Implementations of this interface will be responsible for parsing a JSON input stream and sending the events of tables and rows parsed to {@link TableChangesCallback}.
 * @since 1.0.0
 */
public interface TableChangesJsonParser {
	
	/**
	 * Parses the JSON input stream and sends events of tables and rows parsed to the provided {@link TableChangesCallback}.
	 * @param inputStream the raw JSON input stream
	 * @param tableChangesCallback a callback to receive the tables and rows that were changed
	 * @throws IOException can happen while reading from the input stream or parsing the JSON
	 */
	void parseTableChanges(InputStream inputStream, TableChangesCallback tableChangesCallback) throws IOException;

}