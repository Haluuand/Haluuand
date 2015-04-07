package com.example.entity;

public class BookDir {
	private long position;
	private String dirname;
	private int bookid;
	
	public BookDir(long position, String dirname, int bookid) {
		super();
		this.position = position;
		this.dirname = dirname;
		this.bookid = bookid;
	}
	public int getBookid() {
		return bookid;
	}
	public void setBookid(int bookid) {
		this.bookid = bookid;
	}
	public long getPosition() {
		return position;
	}
	public void setPosition(long position) {
		this.position = position;
	}
	public String getDirname() {
		return dirname;
	}
	public void setName(String dirname) {
		this.dirname = dirname;
	}
	
	
}
