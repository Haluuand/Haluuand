package com.example.entity;

public class MenuInfo {

	public String title;
	public int imgsrc;
	public boolean ishide;
	public int menuId;

	public MenuInfo(int menuId, String title,int imgsrc,Boolean ishide){
		this.menuId=menuId;
		this.title=title;
		this.imgsrc=imgsrc;
		this.ishide=ishide;
	}
}
