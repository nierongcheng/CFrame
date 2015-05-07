package com.codi.frame.net;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

/**
 * 文件缓存类
 * @author Codi
 */
public class FileCache {

	// private static final String LOG_TAG = FileCache.class.getSimpleName();

	private File cacheDir;

	/**
	 * @param context
	 * @param cachePath
	 */
	public FileCache(Context context, String cachePath) {

		cacheDir = new File(cachePath);
		if(!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}

	/**
	 * @param url
	 * @param inputStream
	 */
	public void addToFileCache(String url, InputStream inputStream) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(getFromFileCache(url));
			copyStream(inputStream, outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public File getFromFileCache(String url) {
		String fileName = urlToFileName(url);
		File file = new File(cacheDir, fileName);
		return file;
	}

	/**
	 * 清空文件缓存
	 */
	public void clearCache() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	private String urlToFileName(String url) {
		return String.valueOf(url.hashCode());
	}

	private void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			while(true) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
