package com.example.activity;

import com.example.entity.MenuInfo;
import com.example.reader.R;

import java.util.ArrayList;
import java.util.List;


public class MenuUtils {
	public static final int MENU_LANDSCAPEMODE=1;
	public static final int MENU_SKIP=2;
	public static final int MENU_FONT=3;
	public static final int MENU_EXIT=4;
	public static final int MENU_NIGHTMODE=5;
	public static final int MENU_ADD_BOOKMARK=6;
	public static final int MENU_DIRECTORY_BOOKMARK=7;
	public static final int MENU_OTHER=8;
	
	public static List<MenuInfo> getTxtViewerMenu(){
		List<MenuInfo> list=new ArrayList<MenuInfo>();
//		list.add(new MenuInfo(MENU_LANDSCAPEMODE,"夜间模式",R.drawable.menu_ic_setting,false));
//		list.add(new MenuInfo(MENU_SKIP,"跳转",R.drawable.menu_ic_logout,false));
		list.add(new MenuInfo(MENU_FONT,"字体设置",R.drawable.menu_ic_help,false));
		list.add(new MenuInfo(MENU_NIGHTMODE,"夜间模式",R.drawable.menu_ic_exit,false));
		list.add(0,new MenuInfo(MENU_ADD_BOOKMARK,"增加书签",R.drawable.menu_ic_setting,false));
		list.add(0,new MenuInfo(MENU_DIRECTORY_BOOKMARK,"目录书签",R.drawable.menu_ic_setting,false));
//		list.add(0,new MenuInfo(MENU_EXIT,"退出",R.drawable.menu_ic_setting,false));
//		list.add(0,new MenuInfo(MENU_OTHER,"其它",R.drawable.menu_ic_setting,false));
		
		return list;
	}
	

	public static List<MenuInfo> getMenu(){
		List<MenuInfo> list=new ArrayList<MenuInfo>();		
//			list.add(0,new MenuInfo(MENU_SERCH_FRIEND,"��������",R.drawable.menu_ic_search_friend,false));
//			list.add(0,new MenuInfo(MENU_ADD_GROUP,"��ӷ���",R.drawable.menu_ic_addgroup,false));
//			list.add(0,new MenuInfo(MENU_ADD_FRIEND,"��Ӻ���",R.drawable.menu_ic_addfriend,false));
//			list.add(0,new MenuInfo(MENU_SERCH_FRIEND,"��������",R.drawable.menu_ic_search_friend,false));
		    
			
		return list;
	}
	
}
