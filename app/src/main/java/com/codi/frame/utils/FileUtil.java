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
	 * 创建目录
	 */
	public static void createDirectory(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

    /**
     * Deletes a file or directory, with optional recursion.
     * @param path File or directory path to delete.
     * @param recursive Whether to delete all subdirectories and files.
     */
    public static void delete(String path, boolean recursive) {
        File file = new File(path);
        if(file.exists()) {
            delete(file, recursive);
        }
    }

    /**
     * Deletes a file or directory, with optional recursion.
     * @param path File or directory to delete.
     * @param recursive Whether to delete all subdirectories and files.
     */
    public static void delete(File path, boolean recursive) {
        if (recursive && path.isDirectory()) {
            String[] children = path.list();
            for (String child : children) {
                delete(new File(path, child), recursive);
            }
        }
        path.delete();
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
		FileUtil.delete(destin, true);
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
	 * @param assetName
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
            FileUtil.delete(newFilePath, true);
	    } finally {
	    	try {
				if(in != null) {
					in.close();
				}
				
				if(out != null) {
					out.flush();
			        out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	public static String createHashCode(String url) {
		return String.valueOf(url.hashCode());
	}

    public static String[] readFilenames(String path) {
        File dir = new File(path);
        if(!dir.isDirectory()) {
            throw new IllegalArgumentException(path + " is not a directory.");
        }
        File[] files = dir.listFiles();
        if(files != null) {
            String[] filenames = new String[files.length];
            for(int i = 0, length = files.length; i < length; i++) {
                filenames[i] = files[i].getAbsolutePath();
            }
            return filenames;
        }
        return null;
    }
}