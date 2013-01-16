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
