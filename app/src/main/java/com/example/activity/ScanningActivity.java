package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.reader.R;
import com.example.service.OnFileListCallback;
import com.example.service.ScanSDcard;

public class ScanningActivity extends Activity implements OnClickListener,OnFileListCallback{

	private Button btn_cancel;  
    private RelativeLayout layout; 
    private ScanSDcard mTask; 
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.ativity_scanning);  
        
        btn_cancel = (Button) findViewById(R.id.button_scancancel);    
        layout=(RelativeLayout)findViewById(R.id.window_bottom);  
          
        //
        layout.setOnClickListener(new OnClickListener() {  
              
            public void onClick(View v) {  
                // TODO Auto-generated method stub
            	
            }  
        });  

        btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mTask.cancel(true);
				finish(); 
			}
		});  
        
        mTask = new ScanSDcard();  
        mTask.execute(Environment.getExternalStorageDirectory().getAbsolutePath(), "*.txt",this);

		
    }
    
    public boolean onTouchEvent(MotionEvent event){  
    	mTask.cancel(true);
        finish();  
        return true;  
    }  
  
    public void onClick(View v) {
    	mTask.cancel(true);
        finish();  
    }

	@Override
	public void PostScan() {
		// TODO Auto-generated method stub
		Intent intent1=new Intent(this,ScanResultActivity.class);
     	startActivity(intent1);
     	finish(); 
	}
}
