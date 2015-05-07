package com.codi.frame.net;


import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * 图片缓存类
 * 使用一个强引用和一个软引用，软引用很容易被垃圾回收机制回收
 * @author Codi
 * 
 */
public class ImageCache {

	private static final int HARD_CACHE_CAPACITY = 100;

	private static final int DELAY_BEFORE_PURGE = 10 * 1000; // 缓存清理延迟时间

	// 硬缓存，设置一个合理的容量
	private final HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap<String, Bitmap>(
			HARD_CACHE_CAPACITY / 2, 0.75f, true) {

		private static final long serialVersionUID = -8952621107155814709L;

		@Override
		protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
			if (size() > HARD_CACHE_CAPACITY) {
				// 把从硬缓存中移除的添加到软缓存
				sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			} else {
				return false;
			}
		}
	};

	// 软缓存
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			HARD_CACHE_CAPACITY / 2);

	private final Handler purgeHandler = new Handler();

	private final Runnable purger = new Runnable() {
		public void run() {
			clearCache();
		}
	};

	/**
	 * 添加bitmap到缓存
	 * @param bitmap
	 */
	public void addBitmapToCache(String url, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (sHardBitmapCache) {
				sHardBitmapCache.put(url, bitmap);
			}
		}
	}

	/**
	 * 根据url从缓存中读取bitmap，如果不存在则返回null
	 * @param url
	 */
	public Bitmap getBitmapFromCache(String url) {
		synchronized (sHardBitmapCache) {
			final Bitmap bitmap = sHardBitmapCache.get(url);
			if (bitmap != null) {
				// 把bitmap放到第一个位置，这样就最后一个移除
				sHardBitmapCache.remove(url);
				sHardBitmapCache.put(url, bitmap);
				return bitmap;
			}
		}

		SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null) {
				return bitmap;
			} else {
				// 软缓存已经被垃圾回收机制回收
				sSoftBitmapCache.remove(url);
			}
		}

		return null;
	}

	/**
	 * 清除所有的缓存
	 */
	private void clearCache() {
		sHardBitmapCache.clear();
		sSoftBitmapCache.clear();
	}

	/**
	 * 移除之前的任务并添加一个新的延时
	 */
	public void resetPurgeTimer() {
		purgeHandler.removeCallbacks(purger);
		purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
	}
}
