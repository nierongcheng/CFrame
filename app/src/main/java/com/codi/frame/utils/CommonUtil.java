package com.codi.frame.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Codi on 2015/4/20 0020.
 */
public class CommonUtil {

    /**
     * hide soft input.
     *
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
