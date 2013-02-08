package com.appglu.android.util;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
	
	public static boolean hasInternetConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
	    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
	
	public static Bitmap decodeSampledBitmapFromInputStream(InputStream inputStream) {
	    return BitmapFactory.decodeStream(inputStream);
	}
	
	public static Bitmap decodeSampledBitmapFromInputStream(InputStream inputStream, int inSampleSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		
	    options.inSampleSize = inSampleSize;
	    
	    return BitmapFactory.decodeStream(inputStream, null, options);
	}
	
	public static Bitmap decodeSampledBitmapFromFile(File file) {
		return BitmapFactory.decodeFile(file.getPath());
	}
	
	public static Bitmap decodeSampledBitmapFromFile(File file, int inSampleSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		
	    options.inSampleSize = inSampleSize;
	    
	    return BitmapFactory.decodeFile(file.getPath(), options);
	}
	
	public static Bitmap decodeSampledBitmapFromFile(File file, int requestedWidth, int requestedHeight) {
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    
	    BitmapFactory.decodeFile(file.getPath(), options);
	    
	    int inSampleSize = calculateInSampleSize(options, requestedWidth, requestedHeight);
	    
	    return decodeSampledBitmapFromFile(file, inSampleSize);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int requestedWidth, int requestedHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > requestedHeight || width > requestedWidth) {
	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) requestedHeight);
	        final int widthRatio = Math.round((float) width / (float) requestedWidth);
	
	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	
	    return inSampleSize;
	}
	
}