package com.example.service;

import com.example.entity.Book;
import com.example.entity.FileInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StaticList {
	public static ArrayList<FileInfo> scanlist = new ArrayList<FileInfo>();
	public static List<Book> booklist;
    public static HashMap<String,Long> bookdirlist = new HashMap<String, Long>();
    public static ArrayList<String> bookdirtest = new ArrayList<String>();
}
