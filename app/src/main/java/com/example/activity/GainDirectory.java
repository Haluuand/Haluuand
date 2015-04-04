package com.example.activity;

import android.database.sqlite.SQLiteDatabase;

import com.example.service.StaticList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class GainDirectory {
	private String Setting_text_code;
//	private DBOpenHelter dbOpenHelper;
	private SQLiteDatabase db;
	public GainDirectory() {
		super();
//		this.dbOpenHelper = new DBOpenHelter(mContext);
		
	}


	private void GetTextCode(String path)
	{
		
		try{
			FileInputStream fileIS = new FileInputStream(new File(path));
			BufferedInputStream buf = new BufferedInputStream(fileIS);
			buf.mark(4);
			byte[] first3bytes = new byte[3];
			buf.read(first3bytes);
			buf.reset();
			if(first3bytes[0] == (byte)0xEF && first3bytes[1] == (byte)0xBB && first3bytes[2] == (byte)0xBF) {
				Setting_text_code = "utf-8";
			}else if(first3bytes[0] == (byte)0xFF && first3bytes[1] == (byte)0xFE) {
				Setting_text_code = "unicode";
			}else if(first3bytes[0] == (byte)0xFE && first3bytes[1] == (byte)0xFF) {
				Setting_text_code = "utf-16be";
			}else {
				Setting_text_code = "GBK";
			}
            System.out.println("编码格式：" + Setting_text_code);
			buf.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
		
		
	public void gain(String path) throws IOException{
		
		GetTextCode(path);
//		db = dbOpenHelper.getWritableDatabase();
		FileInputStream fInputStream = new FileInputStream(new File(path));
		InputStreamReader inputStreamReader = new InputStreamReader(fInputStream, Setting_text_code);
		BufferedReader in = new BufferedReader(inputStreamReader);
		String line =  in.readLine();
		//String[] strings = in.r;
		long position = 0;
		int lengthArr = 0;
		String temp;
		while(line!= null){ 
			position += line.length();
			temp = line.trim();
	        lengthArr = temp.length();
	        if (lengthArr > 20)
	        {
	            temp = temp.substring(0, 20);
	        }
	        else {
	            temp = temp.substring(0, lengthArr);
	        }
	        if (temp.contains("第") && (temp.contains("章") || temp.contains("节")))
	        {
	        	//BookDir bDir = new BookDir(position, temp,bookid);
//	    		db.execSQL("insert into bookdir(bookid,name,position) values(?,?,?)",
//	    				new Object[]{bookid,temp,position});
                StaticList.bookdirlist.put(temp,position);
	        }
	        line = in.readLine();	
		}
//		db.close();
		in.close();
		//return dirlist;
	}

}
