package com.codi.login.model;

import android.content.ContentValues;

/**
 * Created by Codi on 2015/5/8 0008.
 */
public class CommonLoginModel extends BaseLoginModel {

    public String username;
    public String password;

    @Override
    public String getStringModel() {
        return null;
    }

    @Override
    public ContentValues getContentValueModel() {
        return null;
    }
}
