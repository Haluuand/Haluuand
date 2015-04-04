package com.example.entity;

public class BookMark {
	private long position;
	private Integer bookmarkId;
	private String datetime;
	private String describe;
	private Integer bookId;
	
	
	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	

	public BookMark(long position, String datetime, String describe,
			Integer bookId) {
		super();
		this.position = position;
		this.datetime = datetime;
		this.describe = describe;
		this.bookId = bookId;
	}

	
	
	public BookMark(long position, Integer bookmarkId, String datetime,
			String describe, Integer bookId) {
		super();
		this.position = position;
		this.bookmarkId = bookmarkId;
		this.datetime = datetime;
		this.describe = describe;
		this.bookId = bookId;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public long getPosition() {
		return position;
	}
	
	public void setPosition(long position) {
		this.position = position;
	}

	public Integer getBookmarkId() {
		return bookmarkId;
	}

	public void setBookmarkId(Integer bookmarkId) {
		this.bookmarkId = bookmarkId;
	}
	

}
