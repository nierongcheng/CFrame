package com.codi.frame.net;

import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.codi.frame.net.image.BitmapLruImageCache;

/**
 * Title: RequestManager
 * Description: 
 * @author Codi
 * @date 2013-11-20
 */
public class MyVolley {

	private static RequestQueue sRequestQueue;
    private static ImageLoader sImageLoader;


    private MyVolley() {
        // no instances
    }


    public static void init(Context context) {
        if (sRequestQueue == null) {
            sRequestQueue = Volley.newRequestQueue(context);
        }

    }

    public static void initImageLoader(Context context) {
        if (sImageLoader == null) {
            int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            // Use 1/8th of the available memory for this memory cache.
            int cacheSize = 1024 * 1024 * memClass / 8;
            sImageLoader = new ImageLoader(sRequestQueue, new BitmapLruImageCache(cacheSize));
        }
    }


    public static RequestQueue getRequestQueue() {
        if (sRequestQueue != null) {
            return sRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }


    /**
     * Returns instance of ImageLoader initialized with {@see FakeImageCache} which effectively means
     * that no memory caching is used. This is useful for images that you know that will be show
     * only once.
     * 
     * @return
     */
    public static ImageLoader getImageLoader() {
        if (sImageLoader != null) {
            return sImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }
	
}
