package com.example.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

import com.example.reader.R;

public class PreferencesService {
	private Context mContext;

	public PreferencesService(Context mContext) {
		this.mContext = mContext;
	}
	
	public void save(Integer textsize,Integer textcolor,Boolean nightmodel){
		SharedPreferences preferences = mContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt("textsize", textsize);
		editor.putInt("textcolor", textcolor);
		editor.putBoolean("nightmodel",nightmodel);
		editor.commit();
	}
	
	public Map<String, Object> getPreferences(){
		Map<String, Object> params = new HashMap<String, Object>();
		SharedPreferences preferences = mContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
		params.put("textsize", preferences.getInt("textsize", 30));
		params.put("textcolor", preferences.getInt("textcolor", Color.BLACK));
		params.put("nightmodel", preferences.getBoolean("nightmodel", false));

		return params;
	}
	
 
}
