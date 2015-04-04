package com.example.service;

import android.os.AsyncTask;

import com.example.entity.FileInfo;

import java.io.File;
import java.util.ArrayList;

public class ScanSDcard extends AsyncTask<Object, Integer, OnFileListCallback>{

	final ArrayList<FileInfo> list = new ArrayList<FileInfo>();		
    
	@Override
	protected void onPostExecute(OnFileListCallback result) {
		StaticList.scanlist = list ;
		result.PostScan();
	}

	@Override
	protected OnFileListCallback  doInBackground(Object...params) {
	// TODO Auto-generated method stub
	String string = (String)params[1];
	String type = string.substring(string.lastIndexOf(".") + 1);
	OnFileListCallback ocb = (OnFileListCallback)params[2];

	File file = new File((String)params[0]);
	
	try {
		scanSDCard(file,type,list);
	}catch (Exception e) {  
                  
		} 
	return ocb;
	
	}
			

private static void scanSDCard(File file, String ext, ArrayList<FileInfo> list) {
	if (file.isDirectory()) {
		File[] files = file.listFiles();
	    if (files != null) {
		        for (int i = 0; i < files.length; i++) {
		            File tmp = files[i];
	                if (tmp.isFile()) {
	                	//if(tmp.length()<102400)
	                		//continue;
		                String fileName = tmp.getName();
		                String filePath = tmp.getName();
		                if (fileName.indexOf(".") >= 0) {
		                    fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
	                        if (ext != null && ext.equalsIgnoreCase(fileName)) {  
		                    FileInfo info = new FileInfo();
		                    info.setFileName(filePath);
		                    info.setFilePath(tmp.getAbsolutePath());
		                    list.add(info);
		                    }
	                    }
	                } else
		            scanSDCard(tmp, ext, list);
	              }
     }
	} else {
		     if (file.isFile()) {      
		         String fileName = file.getName();
		         String filePath = file.getName();
		         if (fileName.indexOf(".") >= 0) {
		             fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
		             if (ext != null && ext.equalsIgnoreCase(fileName)) {
		                 FileInfo info = new FileInfo();
		                 info.setFileName(filePath);
		                 info.setFilePath(file.getAbsolutePath());
		                 list.add(info);			     
		                 }
		             }
		         }
	         }
    }
}



