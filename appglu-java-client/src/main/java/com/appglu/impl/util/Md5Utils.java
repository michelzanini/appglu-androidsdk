package com.appglu.impl.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public abstract class Md5Utils {
	
	public static final int BUFFER_SIZE = 4096;
	
	/**
     * Computes the MD5 hash of the data in the given input stream and returns
     * it as an array of bytes.
     */
    public static byte[] computeMd5Hash(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ( (bytesRead = bis.read(buffer, 0, buffer.length)) != -1 ) {
                messageDigest.update(buffer, 0, bytesRead);
            }
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
        	throw new IOException(e);
		} finally {
            IOUtils.closeQuietly(bis);
        }
    }

    /**
     * Computes the MD5 hash of the given data and returns it as an array of
     * bytes.
     */
    public static byte[] computeMd5Hash(byte[] data) throws IOException {
        return computeMd5Hash(new ByteArrayInputStream(data));
    }
    
    /**
     * Check if the MD5 hash of the given data matches with the eTag (hex of md5 returned by the server)
     * @return true if matches false if not
     */
    public static boolean md5MatchesWithETag(byte[] contentMd5, String eTag) {
		byte[] clientSideHash = contentMd5;
		byte[] serverSideHash = Md5Utils.fromHex(eTag);
		
		return Arrays.equals(clientSideHash, serverSideHash);
    }
    
    /**
     * Converts a Hex-encoded data string to the original byte data.
     *
     * @param hexData hex-encoded data to decode.
     * @return decoded data from the hex string.
     */
    public static byte[] fromHex(String hexData) {
    	if (StringUtils.isEmpty(hexData)) {
    		return new byte[0];
    	}
        byte[] result = new byte[(hexData.length() + 1) / 2];
        String hexNumber = null;
        int stringOffset = 0;
        int byteOffset = 0;
        while (stringOffset < hexData.length()) {
            hexNumber = hexData.substring(stringOffset, stringOffset + 2);
            stringOffset += 2;
            result[byteOffset++] = (byte) Integer.parseInt(hexNumber, 16);
        }
        return result;
    }

}
