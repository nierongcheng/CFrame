package com.codi.frame.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.codi.frame.R;

/**
 * Created by Codi on 2015/4/23 0023.
 */
public class ContactUtil {

    /**
     * query contact name by phone number.
     * @param number
     * @return if not find it, then return null.
     */
    private String queryDisplayNameByNumber(Context context, String number) {
        String nameStr = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        Cursor cursor = context.getContentResolver().query(uri, new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);
        if(cursor == null) {
            return nameStr;
        }

        if(cursor.moveToFirst()) {
            nameStr = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return nameStr;
    }

}
