package com.appglu;

import java.io.IOException;
import java.io.InputStream;

public interface StorageStreamCallback {
	
	void doWithInputStream(InputStream fileStream) throws IOException;

}