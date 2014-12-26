package com.codi.frame.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtil {
     
    /**
     * utilDate ---->string
     * 获得当前时间，将时间格式化
     * yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateString(java.util.Date utilDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = format.format(utilDate);
        return  time;       
    }
    
    /**
     * utilDate ---->string
     * 获得当前时间，将时间格式化
     * yyyy-MM-dd
     * @return
     */
    public static String getDateString1(java.util.Date utilDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String time = format.format(utilDate);
        return time;       
    }

    /**
     * utilDate ---->string
     * 获得当前时间，将时间格式化
     * dd-MM-yyyy HH:mm:ss
     * @return
     */
    public static String getDateString2(java.util.Date utilDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String time = format.format(utilDate);
        return  time;       
    }
    
    /**
     * string---->utilDate
     * 将指定格式的字符串转为utilDate
     * time格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static java.util.Date getUtilDate(String time) {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    	java.util.Date utilDate = null;
		try {
			utilDate = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return utilDate;
    }
     
    /**
     * 将utilDate转为sqlDate
     * @param utilDate
     */
    public static java.sql.Date getSQLDate(java.util.Date utilDate){
        if(utilDate == null){
            utilDate = new java.util.Date();
        }
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return sqlDate;
    }
     
    /**
     * 将sqlDate转为utilDate
     * @param sqlDate
     */
    public static java.util.Date getUtilDate(java.sql.Date sqlDate){
        java.util.Date utilDate = null;
        if(sqlDate == null){
            utilDate = new java.util.Date();
            return utilDate;
        }
        utilDate = new java.util.Date(sqlDate.getTime());
        return utilDate;
    }
}