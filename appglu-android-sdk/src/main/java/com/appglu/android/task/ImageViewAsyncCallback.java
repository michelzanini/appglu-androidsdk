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
package com.appglu.android.task;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.appglu.AsyncCallback;
import com.appglu.ExceptionWrapper;

/**
 * {@link AsyncCallback} implementation to load image Bitmaps into a ImageView, displaying a progress <code>View</code> while the image is being loaded and a place holder <code>View</code> if the image fails loading.<p>
 * 
 * Use the {@link #ImageViewAsyncCallback(android.widget.ImageView, android.view.View, android.view.View)} constructor to load the image into a provided <code>ImageView</code> reference. 
 * If a non <code>null</code> <code>View</code> is provided as the second argument to the constructor, then it will be displayed as long as the image is loading. 
 * If a non <code>null</code> <code>View</code> is provided as the third argument to the constructor, it will be displayed in the case the image fails loading.<p>
 * 
 * Use the {@link #ImageViewAsyncCallback(com.appglu.android.task.ImageViewAsyncCallback.ImageDownloadListener)} constructor to receive callback methods while the image is being loaded.
 * 
 * @see com.appglu.AsyncCallback
 * @since 1.0.0
 */
public class ImageViewAsyncCallback extends AsyncCallback<Bitmap> {
	
	private ImageDownloadListener imageDownloadListener;

	public ImageViewAsyncCallback(ImageDownloadListener imageDownloadListener) {
		this.imageDownloadListener = imageDownloadListener;
	}
	
	/**
	 * @param imageView a <code>ImageView</code> reference from your Activity
	 * @param inProgressView a <code>View</code> reference from your Activity or <code>null</code> if you don't want to show a progress view while loading the image
	 * @param placeholderView a <code>View</code> reference from your Activity or <code>null</code> if you don't want a place holder view to be displayed when an error occur while loading the image
	 */
	public ImageViewAsyncCallback(ImageView imageView, View inProgressView, View placeholderView) {
		this.imageDownloadListener = new DefaultImageDownloadListener(imageView, inProgressView, placeholderView);
	}

	/**
	 * @param id this object will be set as a tag to the views ({@code view.setTag()}) and when the callback returns, the tag will be validated if it has changed or not. <strong>This is import if you use the callback in an adapter</strong>.
	 * @param imageView a <code>ImageView</code> reference from your Activity
	 * @param inProgressView a <code>View</code> reference from your Activity or <code>null</code> if you don't want to show a progress view while loading the image
	 * @param placeholderView a <code>View</code> reference from your Activity or <code>null</code> if you don't want a place holder view to be displayed when an error occur while loading the image
	 */
	public ImageViewAsyncCallback(Object id, ImageView imageView, View inProgressView, View placeholderView) {
		this.imageDownloadListener = new DefaultImageDownloadListener(id, imageView, inProgressView, placeholderView);
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		if (imageDownloadListener != null) {
			imageDownloadListener.onImageStartLoading();
		}
	}

	@Override
	public void onResult(Bitmap bitmap) {
		if (imageDownloadListener != null) {
			imageDownloadListener.onImageLoaded(bitmap);
		}
	}

	@Override
	public void onException(ExceptionWrapper exceptionWrapper) {
		super.onException(exceptionWrapper);
		if (imageDownloadListener != null) {
			imageDownloadListener.onImageFailedLoading();
		}
	}
	
	public static class DefaultImageDownloadListener implements ImageDownloadListener {
		
		private Object id;
		
		private WeakReference<ImageView> imageViewReference;
		private WeakReference<View> inProgressViewReference;
		private WeakReference<View> placeholderViewReference;
		
		public DefaultImageDownloadListener(ImageView imageView, View inProgressView, View view) {
			this.imageViewReference = new WeakReference<ImageView>(imageView);
			this.inProgressViewReference = new WeakReference<View>(inProgressView);
			this.placeholderViewReference = new WeakReference<View>(view);
		}
		
		public DefaultImageDownloadListener(Object id, ImageView imageView, View inProgressView, View placeholderView) {
			this.id = id;
			
			if (imageView != null) {
				imageView.setTag(id);
			}
			if (inProgressView != null) {
				inProgressView.setTag(id);
			}
			if (placeholderView != null) {
				placeholderView.setTag(id);
			}
			
			this.imageViewReference = new WeakReference<ImageView>(imageView);
			this.inProgressViewReference = new WeakReference<View>(inProgressView);
			this.placeholderViewReference = new WeakReference<View>(placeholderView);
		}

		@Override
		public void onImageStartLoading() {
			this.setImageBitmap(null);
			this.setInProgressViewVisibility(View.VISIBLE);
			this.setPlaceholderViewVisibility(View.GONE);
		}

		@Override
		public void onImageLoaded(Bitmap bitmap) {
			this.setImageBitmap(bitmap);
			this.setInProgressViewVisibility(View.GONE);
			this.setPlaceholderViewVisibility(View.GONE);
		}

		@Override
		public void onImageFailedLoading() {
			this.setInProgressViewVisibility(View.GONE);
			this.setPlaceholderViewVisibility(View.VISIBLE);
		}
		
		private void setImageBitmap(Bitmap bitmap) {
			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					if (id == null || id.equals(imageView.getTag())) {
						imageView.setImageBitmap(bitmap);
					}
				}
			}
		}

		private void setInProgressViewVisibility(int visibility) {
			if (inProgressViewReference != null) {
				View inProgressView = inProgressViewReference.get();
				if (inProgressView != null) {
					if (id == null || id.equals(inProgressView.getTag())) {
						inProgressView.setVisibility(visibility);
					}
				}
			}
		}
		
		private void setPlaceholderViewVisibility(int visibility) {
			if (placeholderViewReference != null) {
				View placeholderView = placeholderViewReference.get();
				if (placeholderView != null) {
					if (id == null || id.equals(placeholderView.getTag())) {
						placeholderView.setVisibility(visibility);
					}
				}
			}
		}
		
	}
	
	/**
	 * 
	 * Provide callbacks methods when the image is being downloaded in background.
	 * @since 1.0.0
	 */
	public interface ImageDownloadListener {
		
		/**
		 * Executes before the HTTP request.
		 */
		void onImageStartLoading();
		
		/**
		 * Executes after the HTTP completes with success.
		 */
		void onImageLoaded(Bitmap bitmap);
		
		/**
		 * Executes after the HTTP failed.
		 */
		void onImageFailedLoading();

	}

}
