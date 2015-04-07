package com.example.service;

import com.example.entity.Book;
import com.example.entity.BookDir;
import com.example.entity.FileInfo;

import java.util.ArrayList;
import java.util.List;

public class StaticList {
	public static ArrayList<FileInfo> scanlist = new ArrayList<FileInfo>();
	public static List<Book> booklist;
    public static ArrayList<BookDir> bookdirlist = new ArrayList<BookDir>(1000);
}
