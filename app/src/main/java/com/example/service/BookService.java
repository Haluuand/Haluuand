package com.example.service;

import java.util.ArrayList;
import java.util.List;

import com.example.entity.Book;
import com.example.entity.BookDir;
import com.example.entity.BookMark;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BookService {
private DBOpenHelter dbOpenHelper;
	
	public BookService(Context context){
		this.dbOpenHelper = new DBOpenHelter(context);
	};

	public int getMaxBookId(){
		int maxbookid = 0;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select max(bookid) from bookshelf",null);
		while(cursor.moveToNext()){
			maxbookid = cursor.getInt(cursor.getColumnIndex("max(bookid)"));	
		}
		cursor.close();
		db.close();
		return maxbookid;
	}
	public void addBookDir(BookDir bDir){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into bookdir(bookid,name,position) values(?,?,?)",
				new Object[]{bDir.getBookid(),bDir.getName(),bDir.getPosition()});
		db.close();
	}
	
	public void addBookMark(BookMark bMark){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into bookmark(bookid,position,datetime,describe) values(?,?,?,?)",
				new Object[]{bMark.getBookId(),bMark.getPosition(),bMark.getDatetime(),bMark.getDescribe()});
		db.close();
	}
	public List<BookMark> getBookMark(Integer id){
		List<BookMark> bookmarks = new ArrayList<BookMark>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from bookmark where bookid="+id.toString()+" order by datetime asc",null);
			while(cursor.moveToNext()){
				Integer bookmarkid = cursor.getInt(cursor.getColumnIndex("bookmarkid"));
				Long position = cursor.getLong(cursor.getColumnIndex("position"));
				String datetime = cursor.getString(cursor.getColumnIndex("datetime"));
				String describe = cursor.getString(cursor.getColumnIndex("describe"));
				bookmarks.add(new BookMark(position,bookmarkid,datetime,describe,id));
			}
			cursor.close();
			db.close();
			return bookmarks;
	}
	
	public void deleteBookMark(int id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from bookmark where bookmarkid = ?",new Object[]{id});
		db.close();
	}
	/**
	 *
	 */
	public void save(Book book){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into bookshelf(name,path,position) values(?,?,?)",
				new Object[]{book.getBookName(),book.getBookPath(),book.getPosition()});
		db.close();
	}
	/**
	 *
	 */
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from bookshelf where bookid = ?",
				new Object[]{id});
		db.close();
	}
	/**
	 *
	 */
	public void update(Book book){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update bookshelf set name = ?,path = ?,position = ? where bookid=?",
				new Object[]{book.getBookName(),book.getBookPath(),book.getPosition(),book.getBookId()});
		db.close();
	}
	public void updatePosition(Book book){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update bookshelf set position = ? where bookid=?",
				new Object[]{book.getPosition(),book.getBookId()});
		db.close();
	}
	/**
	 */
	public Book find(Integer id){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from bookshelf where bookid="+id.toString(),
				null);
		if(cursor.moveToFirst()){
			int bookid = cursor.getInt(cursor.getColumnIndex("bookid"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String path = cursor.getString(cursor.getColumnIndex("path"));
			long position = cursor.getLong(cursor.getColumnIndex("position"));
			db.close();
			return new Book(name,path,position,bookid);
		}
		db.close();
		return null;
		
	}
	
	public List<Book> getScrollData(int offset,int maxResult){
		List<Book> books = new ArrayList<Book>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from bookshelf order by bookid asc limit ?,?",
				new String[]{String.valueOf(offset),String.valueOf(maxResult)});
		while(cursor.moveToNext()){
			int bookid = cursor.getInt(cursor.getColumnIndex("bookid"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String path = cursor.getString(cursor.getColumnIndex("path"));
			long position = cursor.getLong(cursor.getColumnIndex("position"));
			books.add(new Book(name,path,position,bookid));
		}
		cursor.close();
		db.close();
		return books;
	}
	
	public long getCount(){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from bookshelf",null);
		cursor.moveToFirst();
		long result = cursor.getLong(0);
		cursor.close();
		db.close();
		return result;
	}
}
