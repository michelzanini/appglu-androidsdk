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
package com.appglu.android.util;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;

/**
 * Contains utility and helper methods.
 * @since 1.0.0
 */
public abstract class AppGluUtils {

	public static void assertNotNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void assertState(boolean state, String message) {
		if (state) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * @return true if the Internet connection is available on the device, false otherwise
	 */
	public static boolean hasInternetConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
	    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
	
	/**
	 * Decodes an InputStream to Bitmap without resizing the image.<br>
	 * Be careful, if the image is big this can become a problem because it will use to much memory.
	 * @param inputStream an input stream to an image
	 * @return the Bitmap
	 */
	public static Bitmap decodeBitmapFromInputStream(InputStream inputStream) {
	    return BitmapFactory.decodeStream(inputStream);
	}
	
	/**
	 * Decodes an InputStream to Bitmap resizing the image to be <code>inSampleSize</code> times smaller then the original.
	 * @param inputStream an input stream to an image
	 * @param inSampleSize how much smaller that the image will be, for example, <code>inSampleSize</code> equals 2 will return an image 1/2 the size of the original
	 * @return the resized Bitmap
	 */
	public static Bitmap decodeSampledBitmapFromInputStream(InputStream inputStream, int inSampleSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		
	    options.inSampleSize = inSampleSize;
	    
	    return BitmapFactory.decodeStream(inputStream, null, options);
	}
	
	/**
	 * Decodes an image File to Bitmap without resizing the image.<br>
	 * Be careful, if the image is big this can become a problem because it will use to much memory.
	 * @param file the image file reference
	 * @return the Bitmap
	 */
	public static Bitmap decodeBitmapFromFile(File file) {
		return BitmapFactory.decodeFile(file.getPath());
	}
	
	/**
	 * Decodes an image File to Bitmap resizing the image to be <code>inSampleSize</code> times smaller then the original.
	 * @param file the image file reference
	 * @param inSampleSize how much smaller that the image will be, for example, <code>inSampleSize</code> equals 2 will return an image 1/2 the size of the original
	 * @return the resized Bitmap
	 */
	public static Bitmap decodeSampledBitmapFromFile(File file, int inSampleSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		
	    options.inSampleSize = inSampleSize;
	    
	    return BitmapFactory.decodeFile(file.getPath(), options);
	}
	
	/**
	 * Decodes an image File to Bitmap resizing the image to fit the <code>requestedWidth</code> and <code>requestedHeight</code> dimensions.
	 * @param file the image file reference
	 * @param requestedWidth the final image width will be close to the requestedWidth (value is in pixels)
	 * @param requestedHeight the final image height will be close to the requestedHeight (value is in pixels)
	 * @return the resized Bitmap
	 */
	public static Bitmap decodeSampledBitmapFromFile(File file, int requestedWidth, int requestedHeight) {
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    
	    BitmapFactory.decodeFile(file.getPath(), options);
	    
	    int inSampleSize = calculateSampleSize(options, requestedWidth, requestedHeight);
	    
	    return decodeSampledBitmapFromFile(file, inSampleSize);
	}
	
	/**
	 * Decodes an image byte[] to Bitmap without resizing the image.<br>
	 * Be careful, if the image is big this can become a problem because it will use to much memory.
	 * @param imageBytes image content in byte[]
	 * @return the Bitmap
	 */
	public static Bitmap decodeBitmapFromByteArray(byte[] imageBytes) {
		return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
	}
	
	/**
	 * Decodes an image byte[] to Bitmap resizing the image to be <code>inSampleSize</code> times smaller then the original.
	 * @param imageBytes image content in byte[]
	 * @param inSampleSize how much smaller that the image will be, for example, <code>inSampleSize</code> equals 2 will return an image 1/2 the size of the original
	 * @return the resized Bitmap
	 */
	public static Bitmap decodeSampledBitmapFromByteArray(byte[] imageBytes, int inSampleSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		
	    options.inSampleSize = inSampleSize;
	    
	    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
	}
	
	/**
	 * Decodes an image byte[] to Bitmap resizing the image to fit the <code>requestedWidth</code> and <code>requestedHeight</code> dimensions.
	 * @param imageBytes image content in byte[]
	 * @param requestedWidth the final image width will be close to the requestedWidth (value is in pixels)
	 * @param requestedHeight the final image height will be close to the requestedHeight (value is in pixels)
	 * @return the resized Bitmap
	 */
	public static Bitmap decodeSampledBitmapFromByteArray(byte[] imageBytes, int requestedWidth, int requestedHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    
	    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
	    
	    int inSampleSize = calculateSampleSize(options, requestedWidth, requestedHeight);
	    
	    return decodeSampledBitmapFromByteArray(imageBytes, inSampleSize);
	}
	
	/**
	 * Giving a max width and height in pixels this method will calculate a good value to use in BitmapFactory.Options.inSampleSize.
	 */
	public static int calculateSampleSize(BitmapFactory.Options options, int requestedWidth, int requestedHeight) {
	    // Raw height and width of image
	    int width = options.outWidth;
	    int height = options.outHeight;
	    int inSampleSize = 1;
	
	    if (width > requestedWidth || height > requestedHeight) {
	        // Calculate ratios of height and width to requested height and width
	        final int widthRatio = Math.round((float) width / (float) requestedWidth);
	        final int heightRatio = Math.round((float) height / (float) requestedHeight);
	
	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	
	    return inSampleSize;
	}
	
	/**
	 * Given a value in DIP (density per pixel) uses the display metrics of the device to return the value in pixels.
	 */
	public static int valueInDipToPixels(Context context, float valueInDip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                valueInDip, context.getResources().getDisplayMetrics());
    }
	
}
