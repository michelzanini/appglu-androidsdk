package com.appglu;

/**
 * Maps a {@link Row} of the database to {@link StorageFile} object.
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
		storageFile.setETag(row.getString("e_tag"));
		storageFile.setDirectoryId(row.getLong("directory_id"));
		
		return storageFile;
	}

}