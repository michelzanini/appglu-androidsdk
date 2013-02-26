package com.appglu.impl.json;

import java.io.IOException;
import java.io.InputStream;

import com.appglu.TableChangesCallback;

public interface TableChangesJsonParser {
	
	void parseTableChanges(InputStream inputStream, TableChangesCallback tableChangesCallback) throws IOException;

}