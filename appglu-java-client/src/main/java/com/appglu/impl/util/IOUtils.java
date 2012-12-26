package com.appglu.impl.util;

import java.io.Closeable;
import java.io.IOException;

public abstract class IOUtils {

	public static void closeQuietly(Closeable closeable) {
		if (closeable == null) {
			return;
		}
		
		try {
			closeable.close();
		} catch (IOException e) {
			
		}
	}
	
}
