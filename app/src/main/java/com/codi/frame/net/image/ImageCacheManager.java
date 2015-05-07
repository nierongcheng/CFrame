package com.codi.frame.net.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.codi.frame.net.MyVolley;
import com.codi.frame.net.RequestManager;

/**
 * Title: ImageCacheManager
 * Description: 
 * @author Codi
 * @date 2013-11-20
 */
public class ImageCacheManager {

	public enum CacheType {
		DISK,
		MEMORY
	}
	
	private static ImageCacheManager mInstance;
	
	private ImageLoader mImageLoader;
	
	private ImageCache mImageCache;
	
	public static ImageCacheManager getInstance() {
		if(mInstance == null) {
			mInstance = new ImageCacheManager();
		}
		return mInstance;
	}
	
	public void init(Context context, String uniqueName, int cacheSize, CompressFormat compressFormat, int quality, CacheType cacheType) {
		switch (cacheType) {
		case DISK:
			mImageCache = new DiskLruImageCache(context, uniqueName, cacheSize, compressFormat, quality);
			break;

		case MEMORY:
			mImageCache = new BitmapLruImageCache(cacheSize);
			break;
			
		default:
			mImageCache = new BitmapLruImageCache(cacheSize);
			break;
		}
		
		mImageLoader = new ImageLoader(MyVolley.getRequestQueue(), mImageCache);
	}
	
	public Bitmap getBitmap(String url) {
		try {
			return mImageCache.getBitmap(createKey(url));
		} catch (NullPointerException e) {
			throw new IllegalStateException("Disk Cache Not initialized");
		}
	}
	
	public void putBitmap(String url, Bitmap bitmap) {
		try {
			mImageCache.putBitmap(createKey(url), bitmap);
		} catch (NullPointerException e) {
			throw new IllegalStateException("Disk Cache Not initialized");
		}
	}
	
	public ImageLoader getImageLoader() {
		return mImageLoader;
	}
	
	private String createKey(String url) {
		return String.valueOf(url.hashCode());
	}
	
}
