package com.codi.frame.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.codi.frame.model.Book;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Title: OrmHelper
 * Description: 
 * @author Codi
 * @date 2013-11-4
 */
public class OrmHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME = "XXX.db";
	private static final int DATABASE_VERSION = 1;
		
	public OrmHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Book.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, Book.class, true);
			onCreate(sqLiteDatabase, connectionSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
