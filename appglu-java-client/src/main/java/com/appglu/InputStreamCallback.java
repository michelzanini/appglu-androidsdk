package com.appglu;

import java.io.IOException;
import java.io.InputStream;

/**
 * TODO
 */
public interface InputStreamCallback {
	
	void doWithInputStream(InputStream inputStream) throws IOException;

}