package com.appglu;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamCallback {
	
	void doWithInputStream(InputStream inputStream) throws IOException;

}