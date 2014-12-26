package com.codi.frame.db;

import android.content.Context;

import com.codi.frame.db.*;
import com.codi.frame.model.Book;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Title: OrmDaoManager
 * Description: 
 * @author Codi
 * @date 2013-11-4
 */
public class OrmDaoManager {

	private static OrmDaoManager manager;
	
	private com.codi.frame.db.OrmHelper helper;
	
	private Dao<Book, Integer> bookDao;
	
	private OrmDaoManager(Context context) {
		if(helper == null) {
			helper = OpenHelperManager.getHelper(context, com.codi.frame.db.OrmHelper.class);
		}
	}
	
	public static OrmDaoManager getOrmDaoManagerInstance(Context context) {
		
		if(manager == null) {
			manager = new OrmDaoManager(context);
		}
		
		return manager;
	}
	
	public com.codi.frame.db.OrmHelper getHelper() {
		return helper;
	}
	
	public ConnectionSource getConnectionSource() {
		return helper.getConnectionSource();
	}
	
	public Dao<Book, Integer> getBookDao() {
		try {
			if(bookDao == null) {
				bookDao = helper.getDao(Book.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bookDao;
	}
	
	public void close() {
		
		releaseHelper();
		
		bookDao = null;
		
		if(manager != null) {
			manager = null;
		}
		
	}
	
	/**
	 * 释放Helper
	 */
    public void releaseHelper() {
        if (helper != null) {
            OpenHelperManager.releaseHelper();
            helper = null;
        }
    }
}
