package com.codi.frame.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Codi on 2015/4/30 0030.
 */
public class JsonUtil {

    /**
     * Reads the contents of this page from storage.
     * @return Page object with the contents of the page.
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    public static JSONObject readJSONFile(File f) throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String readStr;
            while ((readStr = reader.readLine()) != null) {
                stringBuilder.append(readStr);
            }
            return new JSONObject(stringBuilder.toString());
        } finally {
            reader.close();
        }
    }

    /**
     * Convert a JSONArray object to a String Array.
     *
     * @param array a JSONArray containing only Strings
     * @return a String[] with all the items in the JSONArray
     */
    public static String[] jsonArrayToStringArray(JSONArray array) {
        if (array == null) {
            return null;
        }
        String[] stringArray = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            stringArray[i] = array.optString(i);
        }
        return stringArray;
    }

}
