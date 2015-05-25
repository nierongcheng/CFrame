package com.codi.frame.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Title: BitmapUtil
 * Description: 
 * @author Codi
 * @date 2014-6-5
 */
public class BitmapDrawableUtil {

	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	/**
	 * 得到当前页面的截图
	 * @param activity
	 * @param config
	 * @return
	 */
	public static Bitmap getScreenShot(Activity activity, Config config) {
		View view = activity.getWindow().getDecorView().getRootView();
		DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;
		Bitmap b = Bitmap.createBitmap(width, height, config);
		view.draw(new Canvas(b));
		return b;
	}
	
	/**
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public static Drawable zoomDrawable(Resources res, Drawable drawable, int w, int h) {
          int width = drawable.getIntrinsicWidth();
          int height= drawable.getIntrinsicHeight();
          Bitmap oldbmp = drawableToBitmap(drawable); // drawable转换成bitmap
          Matrix matrix = new Matrix();   // 创建操作图片用的Matrix对象
          float scaleWidth = ((float)w / width);   // 计算缩放比例
          float scaleHeight = ((float)h / height);
          matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例
          Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);       // 建立新的bitmap，其内容是对原bitmap的缩放后的图
          oldbmp.recycle();
          return new BitmapDrawable(res, newbmp);       // 把bitmap转换成drawable并返回
    }
	
	/**
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
          int width = drawable.getIntrinsicWidth();   // 取drawable的长宽
          int height = drawable.getIntrinsicHeight();
          Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888: Config.RGB_565;         // 取drawable的颜色格式
          Bitmap bitmap = Bitmap.createBitmap(width, height, config);     // 建立对应bitmap
          Canvas canvas = new Canvas(bitmap);         // 建立对应bitmap的画布
          drawable.setBounds(0, 0, width, height);
          drawable.draw(canvas);      // 把drawable内容画到画布中
          return bitmap;
    }
	
	/**
	 * 将彩色图片转为灰白
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToGrayscaleBitmap(Drawable drawable) {
		drawable.getBounds();
		int width = drawable.getIntrinsicWidth();   // 取drawable的长宽
		int height = drawable.getIntrinsicHeight();
		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888: Config.RGB_565;         // 取drawable的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);     // 建立对应bitmap
		Canvas canvas = new Canvas(bitmap);         // 建立对应bitmap的画布
		drawable.setBounds(0, 0, width, height);
		  
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		drawable.setColorFilter(f);
		drawable.draw(canvas);      // 把drawable内容画到画布中
		return bitmap;
    }

	/**
	 * 释放Drawable
	 * @param drawable 要释放的drawable对象
	 * @param boundView drawable绑定的View对象
	 */
	public static void recycleDrawable(Drawable drawable, View boundView) {
		if (drawable != null) {
			drawable.setCallback(null);
			boundView.unscheduleDrawable(drawable);
		}
	}

	/**
	 * 回收bitmap
	 * @param bitmap 要回收的bitmap对象
	 */
	public static void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}
}
