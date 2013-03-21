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
package com.appglu;

/**
 * Maps a {@link Row} of the database to a {@link StorageFile} object.
 * 
 * @since 1.0.0
 */
public class StorageFileRowMapper implements RowMapper<StorageFile> {

	public StorageFile mapRow(Row row) throws RowMapperException {
		StorageFile storageFile = new StorageFile();
		
		storageFile.setId(row.getLong("id"));
		storageFile.setKey(row.getString("key"));
		storageFile.setName(row.getString("name"));
		storageFile.setContentType(row.getString("content_type"));
		storageFile.setTitle(row.getString("title"));
		storageFile.setSize(row.getLong("size"));
		storageFile.setLastModified(row.getDate("last_modified"));
		storageFile.setUrl(row.getString("url"));
		storageFile.setETag(row.getString("etag"));
		storageFile.setVersion(row.getString("version"));
		storageFile.setDirectoryId(row.getLong("directory_id"));
		
		return storageFile;
	}
	
	public Row mapObject(StorageFile storageFile) throws RowMapperException {
		Row row = new Row();
		
		row.put("id", storageFile.getId());
		row.put("key", storageFile.getKey());
		row.put("name", storageFile.getName());
		row.put("content_type", storageFile.getContentType());
		row.put("title", storageFile.getTitle());
		row.put("size", storageFile.getSize());
		row.putDatetime("last_modified", storageFile.getLastModified());
		row.put("url", storageFile.getUrl());
		row.put("etag", storageFile.getETag());
		row.put("version", storageFile.getVersion());
		row.put("directory_id", storageFile.getDirectoryId());
		
		return row;
	}

}