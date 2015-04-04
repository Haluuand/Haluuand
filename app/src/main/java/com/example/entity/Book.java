package com.example.entity;

public class Book {

		private String bookName;
		private String bookPath;
		private long position;
		private Integer bookId;
		
		public Book() {
			super();
		}

		public Book(Integer bookId, String bookPath, long position) {
			super();
			this.bookPath = bookPath;
			this.bookId = bookId;
			this.position = position;
		}
		
		@Override
		public String toString() {
			return "Book [bookName=" + bookName + ", bookPath=" + bookPath
					+ ", bookId=" + bookId + "]";
		}

		public Book(String bookName, String bookPath, long position) {
			super();
			this.bookName = bookName;
			this.bookPath = bookPath;
			this.position = position;
		}

		public Book(String bookName, String bookPath, long position,
				Integer bookId) {
			super();
			this.bookName = bookName;
			this.bookPath = bookPath;
			this.position = position;
			this.bookId = bookId;
		}


		public String getBookName() {
			return bookName;
		}
		public void setBookName(String bookName) {
			this.bookName = bookName;
		}
		public String getBookPath() {
			return bookPath;
		}
		public void setBookPath(String bookPath) {
			this.bookPath = bookPath;
		}
		public long getPosition() {
			return position;
		}
		public void setPosition(long position) {
			this.position = position;
		}
		public Integer getBookId() {
			return bookId;
		}
		public void setBookId(Integer bookId) {
			this.bookId = bookId;
		}

	
		

	}


