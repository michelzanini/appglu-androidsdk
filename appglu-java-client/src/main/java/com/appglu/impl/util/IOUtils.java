package com.appglu.impl.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class IOUtils {
	
	public static final int BUFFER_SIZE = 4096;
	
	public static int copy(InputStream in, OutputStream out) throws IOException {
		try {
			int byteCount = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			closeQuietly(in);
			closeQuietly(out);
		}
	}

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
