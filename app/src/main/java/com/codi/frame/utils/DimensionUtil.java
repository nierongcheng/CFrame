package com.codi.frame.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.codi.frame.App;

public class DimensionUtil {

	private final float defWidth = 1024.0f;
	private final float defHeight = 768.0f;

	private float width; // 屏幕
	private float height; // 屏幕
	private float density; // 屏幕密度

	public float xScale; // X方向上的缩放比例
	public float yScale; // Y方向上的缩放比例

	/**
	 * 
	 * @param context
	 *            上下
	 * @param width
	 *            模型图的
	 * @param height
	 *            模型图的
	 */
	public DimensionUtil(Context context, float width, float height) {
		this.init(context, width, height);
	}

	public DimensionUtil(Context context) {
		this.init(context, defWidth, defHeight);
	}

	private void init(Context context, float width, float height) {
		DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
		this.width = dm.widthPixels;
		this.height = dm.heightPixels;
		this.density = dm.density;
		this.xScale = this.width / width;
		this.yScale = this.height / height;
	}

	/**
	 * Turn dp to px according to resolution
	 */
	public int dip2px(float dpValue) {
		return (int) (dpValue * this.density + 0.5f);
	}

	/**
	 * Turn px(pixcel) to dp according to resolution
	 */
	public int px2dip(float pxValue) {
		return (int) (pxValue / this.density + 0.5f);
	}
	
	/**
	 * 取转换后的宽
	 */
	public int getTranslateWidth(int width) {
		if(width == 0) {
			return 0;
		}
		return Math.round(this.xScale * width);
	}

	/**
	 * 取转换后的高
	 */
	public int getTranslateHeight(int height) {
		if(height == 0) {
			return 0;
		}
		return Math.round(this.yScale * height);
	}

	/**
	 * 设置原始大小
	 */
	public void setSizeOriginal(View view, int width, int height) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		if (width > 0) {
			layoutParams.width = width;
		}
		if (height > 0) {
			layoutParams.height = height;
		}
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 设置大小
	 */
	public void setSize(View view, int width, int height) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		if (width > 0) {
			layoutParams.width = getTranslateWidth(width);
		}
		if (height > 0) {
			layoutParams.height = getTranslateHeight(height);
		}
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 以水平方向的比例来设置大
	 */
	public void setSizeHorizontal(View view, int width, int height) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		if (layoutParams == null) {
			return;
		}
		if (width > 0) {
			layoutParams.width = getTranslateWidth(width);
		}
		if (height > 0) {
			layoutParams.height = getTranslateWidth(height);
		}
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 以垂直方向的比例来设置大
	 */
	public void setSizeVertical(View view, int width, int height) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		if (width > 0) {
			layoutParams.width = getTranslateHeight(width);
		}
		if (height > 0) {
			layoutParams.height = getTranslateHeight(height);
		}
		view.setLayoutParams(layoutParams);
	}
	
	/**
	 * 设置原始边距
	 */
	public void setMarginOriginal(View view, int left, int top, int right, int bottom) {
		ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
		layoutParams.leftMargin = left;
		layoutParams.topMargin = top;
		layoutParams.rightMargin = right;
		layoutParams.bottomMargin = bottom;
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 设置边距
	 */
	public void setMargin(View view, int left, int top, int right, int bottom) {
		ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
		layoutParams.leftMargin = getTranslateWidth(left);
		layoutParams.topMargin = getTranslateHeight(top);
		layoutParams.rightMargin = getTranslateWidth(right);
		layoutParams.bottomMargin = getTranslateHeight(bottom);
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 以水平方向的比例来设置边
	 */
	public void setMarginHorizontal(View view, int left, int top, int right, int bottom) {
		ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
		layoutParams.leftMargin = getTranslateWidth(left);
		layoutParams.topMargin = getTranslateWidth(top);
		layoutParams.rightMargin = getTranslateWidth(right);
		layoutParams.bottomMargin = getTranslateWidth(bottom);
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 以垂直方向的比例来设置边
	 */
	public void setMarginVertical(View view, int left, int top, int right, int bottom) {
		ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
		layoutParams.leftMargin = getTranslateHeight(left);
		layoutParams.topMargin = getTranslateHeight(top);
		layoutParams.rightMargin = getTranslateHeight(right);
		layoutParams.bottomMargin = getTranslateHeight(bottom);
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 设置内边
	 */
	public void setPadding(View view, int left, int top, int right, int bottom) {
		view.setPadding(getTranslateWidth(left), getTranslateHeight(top), getTranslateWidth(right), getTranslateHeight(bottom));
	}

	/**
	 * 以水平方向的比例来设置内边距
	 */
	public void setPaddingHorizontal(View view, int left, int top, int right, int bottom) {
		view.setPadding(getTranslateWidth(left), getTranslateWidth(top), getTranslateWidth(right), getTranslateWidth(bottom));
	}

	/**
	 * 以垂直方向的比例来设置内边距
	 */
	public void setPaddingVertical(View view, int left, int top, int right, int bottom) {
		view.setPadding(getTranslateHeight(left), getTranslateHeight(top), getTranslateHeight(right), getTranslateHeight(bottom));
	}
	
	/**
	 * 使长宽按垂直方向缩放的View能居中显示
	 * @param view
	 * @param width
	 */
	public void reCenterHWidthToVWidth(View view, int width) {
		ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
		int distance = getTranslateWidth(width) - getTranslateHeight(width);
		params.leftMargin += distance / 2;
		params.rightMargin += distance / 2;
	}
	
	/**
	 * 使长宽按水平方向缩放的View能居中显示
	 * @param view
	 * @param width
	 */
	public void reCenterVWidthToHWidth(View view, int width) {
		ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
		int distance = getTranslateHeight(width) - getTranslateWidth(width);
		params.leftMargin += distance / 2;
		params.rightMargin += distance / 2;
	}
	
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	/**
	 * 取系统菜单的高度
	 */
	public int getSystemMenuHeight() {
		if (this.height > 600) {
			return 50;
		} else {
			return 42;
		}
	}

    /**
     * 调整字体大小适应屏幕分辨率
     * @param dimenId
     * @return
     */
    public float setTextSizeFixScreenWidth(int dimenId) {
        Resources resources = App.getInstance().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float spValue =resources.getDimension(dimenId);
        int size = (int) (spValue * displayMetrics.widthPixels / (1024.0f * displayMetrics.density) + 0.5f);
        return size;
    }
}