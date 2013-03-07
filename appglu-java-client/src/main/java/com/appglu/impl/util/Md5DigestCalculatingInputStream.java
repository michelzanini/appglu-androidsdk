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
package com.appglu.impl.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Simple InputStream wrapper that examines the wrapped stream's contents as
 * they are read and calculates and MD5 digest.
 */
public class Md5DigestCalculatingInputStream extends FilterInputStream {
    
    /** The MD5 message digest being calculated by this input stream */
    private MessageDigest digest;
    
    public Md5DigestCalculatingInputStream(InputStream in) throws IOException {
        super(in);
        
        try {
        	digest = MessageDigest.getInstance("MD5");
	    } catch (NoSuchAlgorithmException e) {
	    	throw new IOException(e);
	    }
    }

    public byte[] getMd5Digest() {
        return digest.digest();
    }

    /**
     * Resets the wrapped input stream and the in progress message digest.
     */
    @Override
    public synchronized void reset() throws IOException {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        	throw new IOException(e);
        }

        in.reset();
    }

    @Override
    public int read() throws IOException {
        int ch = in.read();
        if (ch != -1) {
            digest.update((byte)ch);
        }
        return ch;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int result = in.read(b, off, len);
        if (result != -1) {
            digest.update(b, off, result);
        }
        return result;
    }

}
