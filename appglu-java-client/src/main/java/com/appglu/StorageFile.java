package com.appglu;

import java.io.Serializable;
import java.util.Date;

public class StorageFile implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String key;
	
	private String name;
	
	private String contentType;
	
	private String title;
	
	private int size;
	
	private Date lastModified;
	
	private String url;
	
	private int directoryId;
	
	public StorageFile() {
		
	}
	
	public StorageFile(int id) {
		this.id = id;
	}

	public StorageFile(String url) {
		this.url = url;
	}

	public int getId() {
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

	public int getSize() {
		return size;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public String getUrl() {
		return url;
	}

	public int getDirectoryId() {
		return directoryId;
	}

	public void setId(int id) {
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

	public void setSize(int size) {
		this.size = size;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setDirectoryId(int directoryId) {
		this.directoryId = directoryId;
	}

	@Override
	public String toString() {
		return "StorageFile [name=" + name + ", url=" + url + "]";
	}
	
}