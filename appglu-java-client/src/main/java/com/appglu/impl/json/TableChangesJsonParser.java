/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
