package com.codi.frame.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Codi on 2015/4/20 0020.
 */
public class CommonUtil {

    /**
     * show or hide soft input.
     *
     * @param context
     */
    public static void toggleSoftInput(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * check whether app has some permission
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermission(Context context, String permission) {
        try {
            return context.checkPermission(permission, Binder.getCallingPid(), Binder.getCallingUid()) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
