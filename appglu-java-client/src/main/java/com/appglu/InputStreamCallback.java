package com.appglu;

import java.io.IOException;
import java.io.InputStream;

/**
 * A callback to have direct access over the <code>InputStream</code>.<br>
 * Usually used when downloading a large file or JSON.
 * 
 * @since 1.0.0
 */
public interface InputStreamCallback {
	
	void doWithInputStream(InputStream inputStream) throws IOException;

}