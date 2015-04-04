package com.example.entity;

public class BookDir {
	private long position;
	private String name;
	private int bookid;
	
	public BookDir(long position, String name, int bookid) {
		super();
		this.position = position;
		this.name = name;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
