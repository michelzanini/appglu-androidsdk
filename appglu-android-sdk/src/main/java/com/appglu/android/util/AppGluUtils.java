package com.appglu.android.util;

import java.io.File;

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
	
	public static boolean hasInternetConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
	    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
	
	public static Bitmap decodeSampledBitmapFromFile(File file, int requestedWidth, int requestedHeight) {
	    // First decode with inJustDecodeBounds = true to check dimensions
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(file.getPath(), options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, requestedWidth, requestedHeight);
	    
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    
	    return BitmapFactory.decodeFile(file.getPath(), options);
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