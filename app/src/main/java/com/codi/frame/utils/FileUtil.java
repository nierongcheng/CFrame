package com.codi.frame.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

/**
 * 文件工具类
 * 
 * @author Codi
 */
public class FileUtil {

	/**
	 * 判断SD卡是否存在
	 */
	public static boolean hasSdcard() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 判断文件是否存在
	 */
	public static boolean isFileExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 删除文件夹和里面所有的文件
	 */
	public static void deleteFloder(String path) {
		File file = new File(path);

		if (!file.exists())
			return;

		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				deleteFloder(childFiles[i].getPath());
			}
			file.delete();
		}
	}

	/**
	 * 创建目录
	 */
	public static void createDirectory(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 删除文件或目录
	 */
	public static void deleteFile(String path) {
		if (path == null)
			return;

		File file = new File(path);
		if (!file.exists()) // 不存在
			return;

		if (file.isFile()) { // 是文件
			file.delete();
		}
		if (file.isAbsolute()) { // 是目录
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) { // 子目录为空
				file.delete();
				return;
			}
			for (File f : childFiles) {
				FileUtil.deleteFile(f.getPath());
			}
			file.delete();
		}
	}

	/**
	 * 取得文件名，不要后缀<br>
	 * 比如 audio.mp3 --> audio
	 */
	public static String getNameNoSuffix(File file) {
		if (file == null) {
			return null;
		}
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (index > 0) {
			return name.substring(0, index);
		}
		return name;
	}

	/**
	 * 复制文件的方法 // 以32KB的速度复制
	 */
	public static boolean copyFile(String source, String destin) {
		File sourceFile = new File(source);
		if (!sourceFile.exists()) // 文件不存在
			return false;

		File destinFile = new File(destin);
		destinFile.getParentFile().mkdirs();
		deleteFile(destin);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			byte[] buffer = new byte[1024 * 32]; // 32K的缓存
			fis = new FileInputStream(sourceFile);
			fos = new FileOutputStream(destinFile);
			int readIndex = -1;
			do {
				readIndex = fis.read(buffer);
				if (readIndex > 0) {
					fos.write(buffer, 0, readIndex);
				}
			} while (readIndex > 0);
		} catch (Exception e) {
			e.printStackTrace();
			destinFile.delete();
			return false;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public static void copyAssetDir(Context context, String assetPath, String destinPath) {
	    AssetManager assetManager = context.getAssets();
	    String assets[] = null;
	    try {
	        assets = assetManager.list(assetPath);
	        if (assets.length == 0) {
	            return;
	        } else {
	        	createDirectory(destinPath);
	            for (int i = 0; i < assets.length; ++i) {
	            	copyAssetFile(context, assetPath, assets[i], destinPath);
	            }
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}
	
	/**
	 * 拷贝Asset文件到目标位置
	 * @param context
	 * @param filename
	 * @param destinPath
	 */
	public static void copyAssetFile(Context context, String assetPath, String assetName, String destinPath) {
	    AssetManager assetManager = context.getAssets();
	    
	    InputStream in = null;
	    OutputStream out = null;
	    String assetFilePath = assetPath + "/" + assetName;
	    String newFilePath = destinPath + "/" + createHashCode(assetName);
	    try {
	        in = assetManager.open(assetFilePath);
	        out = new FileOutputStream(newFilePath);
	 
	        byte[] buffer = new byte[1024];
	        int read;
	        while ((read = in.read(buffer)) != -1) {
	            out.write(buffer, 0, read);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        deleteFile(newFilePath);
	    } finally {
	    	try {
				if(in != null) {
					in.close();
					in = null;
				}
				
				if(out != null) {
					out.flush();
			        out.close();
			        out = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	public static String createHashCode(String url) {
		return String.valueOf(url.hashCode());
	}
}