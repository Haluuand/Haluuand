package com.example.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelter extends SQLiteOpenHelper {

	public DBOpenHelter(Context context) {
		super(context,"bookread.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE bookshelf(bookid integer primary key autoincrement,name,path,position)");
		db.execSQL("CREATE TABLE bookmark(bookmarkid integer primary key autoincrement,bookid,position,datetime,describe)");
		db.execSQL("CREATE TABLE bookdir(bookdirid integer primary key autoincrement,bookid,name,position)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	
		
	}

}
