package com.appglu.android;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.appglu.AsyncCallback;

public class ImageViewAsyncCallback extends AsyncCallback<Bitmap> {

	private WeakReference<ImageView> imageViewReference;
	private WeakReference<ProgressBar> progressBarReference;

	public ImageViewAsyncCallback(ImageView imageView, ProgressBar progressBar) {
		this.imageViewReference = new WeakReference<ImageView>(imageView);
		this.progressBarReference = new WeakReference<ProgressBar>(progressBar);
	}

	public void onPreExecute() {
		super.onPreExecute();
		this.setImageBitmap(null);
		this.setProgressBarVisibility(View.VISIBLE);
	}

	public void onResult(Bitmap bitmap) {
		this.setImageBitmap(bitmap);
	}

	public void onFinish() {
		super.onFinish();
		this.setProgressBarVisibility(View.GONE);
	}

	protected void setImageBitmap(Bitmap bitmap) {
		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null) {
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	protected void setProgressBarVisibility(int visibility) {
		if (progressBarReference != null) {
			ProgressBar progressBar = progressBarReference.get();
			if (progressBar != null) {
				progressBar.setVisibility(visibility);
			}
		}
	}

}