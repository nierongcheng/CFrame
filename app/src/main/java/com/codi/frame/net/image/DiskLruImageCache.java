package com.codi.frame.net.image;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.jakewharton.disklrucache.DiskLruCache;

/**
 * Title: DiskLruImageCache
 * Description: 
 * @author Codi
 * @date 2013-11-20
 */
public class DiskLruImageCache implements ImageCache {

	private DiskLruCache mDiskLruCache;
	private CompressFormat mCompressFormat = CompressFormat.JPEG;
	private static int IO_BUFFER_SIZE = 8 * 1024;
	private int mCompressQuality = 70;
	private static final int APP_VERSION = 1;
	private static final int VALUE_COUNT = 1;
	
	public DiskLruImageCache(Context context, String uniqueName, int diskCacheSize,
			CompressFormat compressFormat, int quality) {
		try {
			final File diskCacheDir = getDiskCacheDir(context, uniqueName);
			mDiskLruCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize);
			mCompressFormat = compressFormat;
			mCompressQuality = quality;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private File getDiskCacheDir(Context context, String uniqueName) {
		final String cachePath = context.getCacheDir().getPath();
		return new File(cachePath + File.separator + uniqueName);
	}
	
	@Override
	public Bitmap getBitmap(String key) {
		
		Bitmap bitmap = null;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskLruCache.get(key);
			if(snapshot == null)
				return null;
			
			final InputStream in = snapshot.getInputStream(0);
			if(in != null) {
				final BufferedInputStream buffIn = new BufferedInputStream(in, IO_BUFFER_SIZE);
				bitmap = BitmapFactory.decodeStream(buffIn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(snapshot != null) {
				snapshot.close();
			}
		}
		return bitmap;
	}

	@Override
	public void putBitmap(String key, Bitmap data) {
		
		DiskLruCache.Editor editor = null;
		try {
			editor = mDiskLruCache.edit(key);
			if(editor == null)
				return;
			
			if(writeBitmapToFile(data, editor)) {
				mDiskLruCache.flush();
				editor.commit();
			} else {
				editor.abort();
			}
		} catch (Exception e) {
			try {
				if(editor != null) {
					editor.abort();
				}
			} catch (IOException e2) {
			}
		}
		
	}
	
	private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor) throws IOException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(editor.newOutputStream(0), IO_BUFFER_SIZE);
			return bitmap.compress(mCompressFormat, mCompressQuality, out);
		} finally {
			if(out != null) {
				out.close();
			}
		}
	}
	
	public boolean containsKey(String key) {
		boolean contained = false;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskLruCache.get(key);
			contained = snapshot != null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(snapshot != null) {
				snapshot.close();
			}
		}
		return contained;
	}

	public void clearCache() {
		try {
			mDiskLruCache.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public File getCacheFolder() {
		return mDiskLruCache.getDirectory();
	}
	
}
