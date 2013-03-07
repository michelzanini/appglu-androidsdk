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

import java.io.Serializable;
import java.util.Date;

import com.appglu.impl.util.StringUtils;

/**
 * Represents a file. Can be used either by the Storage or the Sync APIs.<br>
 * 
 * @see StorageOperations
 * @see SyncOperations
 * @since 1.0.0
 */
public class StorageFile implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long id;
	
	private String key;
	
	private String name;
	
	private String contentType;
	
	private String title;
	
	private long size;
	
	private Date lastModified;
	
	private String url;
	
	private String eTag;
	
	private long directoryId;
	
	public StorageFile() {
		
	}
	
	public StorageFile(long id) {
		this.id = id;
	}

	public StorageFile(String url) {
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getContentType() {
		return contentType;
	}

	public String getTitle() {
		return title;
	}

	public long getSize() {
		return size;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public String getUrl() {
		return url;
	}
	
	public String getETag() {
		return eTag;
	}

	public long getDirectoryId() {
		return directoryId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setETag(String eTag) {
		this.eTag = eTag;
	}

	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	
	public boolean hasId() {
		return id > 0;
	}
	
	public boolean hasKey() {
		return StringUtils.isNotEmpty(key);
	}
	
	public boolean hasUrl() {
		return StringUtils.isNotEmpty(url);
	}
	
	public boolean hasETag() {
		return StringUtils.isNotEmpty(eTag);
	}
	
	public boolean hasLastModifiedDate() {
		return lastModified != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eTag == null) ? 0 : eTag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StorageFile other = (StorageFile) obj;
		if (eTag == null) {
			if (other.eTag != null)
				return false;
		} else if (!eTag.equals(other.eTag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StorageFile [id=" + id + ", url=" + url + ", eTag=" + eTag + "]";
	}
	
}
