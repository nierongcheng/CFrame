package com.codi.frame.net.image;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Title: BitmapLruImageCache
 * Description: 
 * @author Codi
 * @date 2013-11-20
 */
public class BitmapLruImageCache extends LruCache<String, Bitmap> implements ImageCache {

	public BitmapLruImageCache(int maxSize) {
		super(maxSize);
	}
	
	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}


}
