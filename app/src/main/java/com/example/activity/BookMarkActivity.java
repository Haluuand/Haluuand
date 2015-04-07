package com.example.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.Book;
import com.example.entity.BookMark;
import com.example.reader.R;
import com.example.service.BookService;

import java.util.List;

public class BookMarkActivity extends Activity {
private TabHost mTabHost;
	
	private ListView mListView;
    private int bookid;
    private BookService bService;
    private List<BookMark> bookMarks;
    private BookmarkAdapter adapter;
    private Context mContext = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bookmark);
	
		Intent intent=getIntent();
		bookid = intent.getIntExtra("id", 0);
		System.out.println("id:"+bookid);
		bService = new BookService(this);
		bookMarks = bService.getBookMark(bookid); 

		if(bookMarks.size() == 0){
			Toast.makeText(this, "没有书签" ,Toast.LENGTH_SHORT).show();
		}
		mTabHost = (TabHost) findViewById(R.id.tabhost1);
		mTabHost.setup();
		
		
		
		View []viewtabs = new View[]{null,null,null,null};
		int []tabids=new int[]{R.id.tab1,R.id.tab2};
		String[]tabnames=new String[]{"书签","目录"};
		
		
		for(int i=0;i<tabids.length;i++)
		{ 
			 viewtabs[i]=LayoutInflater.from(this).inflate(R.layout.head_bookmark_tabs,null);
			 TextView tv=(TextView)viewtabs[i].findViewById(R.id.tv1);
			 tv.setText(tabnames[i]);
			 mTabHost.addTab(mTabHost.newTabSpec(tabnames[i]).setIndicator(viewtabs[i])
			 .setContent(tabids[i]));
		}
		
		mListView = (ListView)findViewById(R.id.bookmark_listView);
	    adapter = new BookmarkAdapter();
	    mListView.setAdapter(adapter);    
	    mListView.setItemsCanFocus(false);       
	        //
	    mListView.setOnItemClickListener(new OnItemClickListener() {
	            @Override    
	            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
	                    long arg3) {              
	            	Book book = bService.getBookById(bookid);
	            	System.out.println(book.toString());
	            	Intent intent = new Intent(BookMarkActivity.this,TxtViewerActivity.class);
		            intent.putExtra("position",bookMarks.get(position).getPosition());
		            intent.putExtra("path", book.getBookPath());
		            intent.putExtra("id",bookid);
					startActivity(intent);
	            }  
	    });
	    registerForContextMenu(mListView);

	}
	
	 public void onCreateContextMenu(ContextMenu menu, View v, 
	            ContextMenuInfo menuInfo) { 
	         
	        MenuInflater mInflater = getMenuInflater(); 
	        mInflater.inflate(R.menu.bookmark_contextmenu, menu); 
	         
	        super.onCreateContextMenu(menu, v, menuInfo); 
	    } 

	    @Override 
	 public boolean onContextItemSelected(MenuItem item) {
	    	 AdapterContextMenuInfo itemInfo = (AdapterContextMenuInfo)item.getMenuInfo();  
	         int position = itemInfo.position;  
	         //
	        switch(item.getItemId()){ 
	        case R.id.bookmark_goto: 
	            Book book = bService.getBookById(bookid);
            	System.out.println(book.toString());
            	Intent intent = new Intent(BookMarkActivity.this,TxtViewerActivity.class);
	            intent.putExtra("position",bookMarks.get(position).getPosition());
	            intent.putExtra("path", book.getBookPath());
	            intent.putExtra("id",bookid);
				startActivity(intent);
	            break; 
	        case R.id.bookmark_delete: 
	            bService.deleteBookMark(bookMarks.get(position).getBookmarkId());
	            bookMarks = bService.getBookMark(bookid); 
	            adapter = new BookmarkAdapter();
	    	    mListView.setAdapter(adapter); 
	            break; 
	        } 
	        return super.onContextItemSelected(item); 
	    }

     public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.scanresult, menu);
			return true;
		}

     public boolean onOptionsItemSelected(MenuItem item) {
	        // TODO Auto-generated method stub
		 switch(item.getItemId()) {
		    case R.id.button_import:
		    	Toast.makeText(getApplicationContext(), "bookmark",
	                    Toast.LENGTH_SHORT).show();
				finish();
		     	break;	    
		 	}
		 return true;
		}

    private class BookmarkAdapter extends BaseAdapter {

        private LayoutInflater inflater = LayoutInflater.from(mContext);

        @SuppressLint("UseSparseArrays")
        public BookmarkAdapter() {
            super();
        }


        public int getCount() {
            return bookMarks.size();
        }

        public Object getItem(int position) {
            return bookMarks.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_bookmark, null);
                holder.tv1 =
                        (TextView) convertView.findViewById(R.id.bookmark_textView1);
                holder.tv2 =
                        (TextView) convertView.findViewById(R.id.bookmark_textView2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv1.setText(bookMarks.get(position).getDatetime());
            holder.tv2.setText(bookMarks.get(position).getDescribe());

            return convertView;
        }


    }
    private static class ViewHolder {
        public TextView tv1;
        public TextView tv2;
    }
}
