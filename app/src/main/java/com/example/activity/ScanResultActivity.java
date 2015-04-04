package com.example.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.Book;
import com.example.reader.R;
import com.example.service.BookService;
import com.example.service.StaticList;

import java.util.ArrayList;
import java.util.HashMap;


public class ScanResultActivity extends Activity{

    private Context mContext = this;
	private ListView listView;  
    private ArrayList<String> listData;
    private ScanResultAdapter adapter;
    
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.activity_scanresult); 
        
        listView = (ListView)findViewById(R.id.listview_scanresult);
        listData = new ArrayList<String>();
        for (int i = 0; i < StaticList.scanlist.size(); i++){
            listData.add(StaticList.scanlist.get(i).getFileName());
        }
        //initData();
        adapter = new ScanResultAdapter();
        listView.setAdapter(adapter);    
        listView.setItemsCanFocus(false);    
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);    
        //
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {              
                ViewHolder holder = (ViewHolder) arg1.getTag();
                holder.cb.toggle();
                adapter.getIsSelected().put(arg2, holder.cb.isChecked());
            }
        });
	}

//	private void initData() {
//		listData.clear();
//		for (int i = 0; i < StaticList.scanlist.size(); i++) {
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("content", StaticList.scanlist.get(i).getFileName());
//            listData.add(map);
//        }
//	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scanresult, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
	 switch(item.getItemId()) {
	    case R.id.button_import:
	    	//GainDirectory gDirectory = new GainDirectory(ScanResultActivity.this);
	    	for(int i=0;i<adapter.getIsSelected().size();i++){
				if(adapter.getIsSelected().get(i)){
					String filename = StaticList.scanlist.get(i).getFileName();
					String bookname = filename.substring(0,filename.lastIndexOf("."));
					Book book = new Book(bookname, StaticList.scanlist.get(i).getFilePath(),0);
					BookService bookService = new BookService(ScanResultActivity.this);
					bookService.save(book);
//					int id = bookService.getMaxBookId();
//					try {
//						gDirectory.gain(id,StaticList.list.get(i).getFilePath());
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
			}
	    	Toast.makeText(getApplicationContext(), "导入成功！",
                    Toast.LENGTH_SHORT).show();
			finish();
	     	break;	    
	 	}
	 return true;
	}

    public class ScanResultAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private HashMap<Integer, Boolean> isSelected;
        @SuppressLint("UseSparseArrays")
        public ScanResultAdapter(){
            isSelected = new HashMap<Integer, Boolean>();
            for(int i=0; i<listData.size();i++) {
                getIsSelected().put(i,false);
            }
            inflater = LayoutInflater.from(mContext);
        }


        public int getCount() {
            return listData.size();
        }

        public Object getItem(int position) {
            return listData.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_scanresult, null);
                holder.tv =
                        (TextView) convertView.findViewById(R.id.setting_list_item_text);
                holder.cb =
                        (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(listData.get(position).toString());
            holder.cb.setChecked(getIsSelected().get(position));

            return convertView;
        }
        public HashMap<Integer,Boolean> getIsSelected() {
            return isSelected;
        }
    }
    private static class ViewHolder{
        public TextView tv;
        public CheckBox cb;
    }
}
