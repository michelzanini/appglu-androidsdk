package com.appglu.impl.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

public abstract class HashUtils {
	
	public static final int BUFFER_SIZE = 8192;
	
	/**
     * Computes the MD5 hash of the data in the given input stream and returns
     * it as an array of bytes.
     */
    public static byte[] computeMd5Hash(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
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
     * Computes the MD5 hash of the given String and returns it as an array of
     * bytes.
     */
    public static byte[] computeMd5Hash(String data) throws IOException {
    	try {
			return computeMd5Hash(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return computeMd5Hash(data.getBytes());
		}
    }
    
    /**
     * Check if the MD5 hash of the given data matches with the eTag (hex of md5 returned by the server)
     * @return true if matches false if not
     */
    public static boolean md5MatchesWithETag(byte[] contentMd5, String eTag) {
		byte[] clientSideHash = contentMd5;
		byte[] serverSideHash = HashUtils.fromHex(eTag);
		
		return Arrays.equals(clientSideHash, serverSideHash);
    }
    
    /**
     * Converts a string to a Hex-encoded string.
     *
     * @param data data to hex encode.
     * @return hex-encoded string.
     */
    public static String toHexString(String data) {
    	try {
			return toHex(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return toHex(data.getBytes());
		}
    }
    
    /**
     * Converts a Hex-encoded data string to the original String data.
     *
     * @param hexData hex-encoded data to decode.
     * @return decoded data from the hex string.
     */
    public static String fromHexString(String hexData) {
    	byte[] hexBytes = fromHex(hexData);
		try {
			return new String(hexBytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return new String(hexBytes);
		}
    }
    
    /**
     * Converts byte data to a Hex-encoded string.
     *
     * @param data data to hex encode.
     * @return hex-encoded string.
     */
    public static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(data[i]);
            if (hex.length() == 1) {
                // Append leading zero.
                sb.append("0");
            } else if (hex.length() == 8) {
                // Remove ff prefix from negative numbers.
                hex = hex.substring(6);
            }
            sb.append(hex);
        }
        return sb.toString().toLowerCase(Locale.US);
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
