package com.example.activity;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.BookDir;
import com.example.reader.R;
import com.example.service.BookService;
import com.example.service.StaticList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MyshelfActivity extends Activity{

	private GridView bookShelf;
	private MyshelfAdapter adapter;
	private BookService bService;
	private Context mContext = this;
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer,Boolean> isSelected = new HashMap<Integer, Boolean>();
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ArrayList<String> menuLists;
    private ArrayAdapter<String> draweradapter;


    //private GainDirectory gain = new GainDirectory();
    private String Setting_text_code;


	protected void onResume(){

        adapter=new MyshelfAdapter();
        bookShelf.setAdapter(adapter);
        super.onResume();
	}
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myshelf);
		ActionBar actionBar = getActionBar();
		//actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

		bService = new BookService(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        menuLists = new ArrayList<String>();
        for(int i=1; i<=5; i++){
            menuLists.add("test" + i);
        }


        draweradapter = new ArrayAdapter<String>(MyshelfActivity.this , android.R.layout.simple_list_item_1 , menuLists);
        mDrawerList.setAdapter(draweradapter);
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Fragment contentFragment = new ContentFragment();


//                Bundle args = new Bundle();
//                args.putString("text" , menuLists.get(position));

//                contentFragment.setArguments(args);



//                FragmentManager fm = getFragmentManager();

//                fm.beginTransaction().replace(R.id.content_frame , contentFragment).commit();
                Toast.makeText(MyshelfActivity.this,"position"+position,Toast.LENGTH_SHORT).show();
                if (position == menuLists.size()-1){
                    menuLists.add("this is added.");
                }
                draweradapter = new ArrayAdapter<String>(MyshelfActivity.this , android.R.layout.simple_list_item_1 , menuLists);
                mDrawerList.setAdapter(draweradapter);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(MyshelfActivity.this , mDrawerLayout , R.drawable.ic_drawer , R.string.drawer_open , R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);

//                mtitle = getActionBar().getTitle().toString();
//                getActionBar().setTitle("Drawerlayout");

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);

//                getActionBar().setTitle(mtitle);

                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);











        bookShelf = (GridView) findViewById(R.id.bookShelf);

        adapter=new MyshelfAdapter();
        bookShelf.setAdapter(adapter);
        bookShelf.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        bookShelf.setMultiChoiceModeListener(new MultiChoiceModeListener(this));        
        bookShelf.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

                 try{
                     gaindir(position);

                 }catch (IOException e){
                     e.printStackTrace();
                 }

                 Intent intent = new Intent(mContext,TxtViewerActivity.class);
                 intent.putExtra("path",StaticList.booklist.get(position).getBookPath());
                 intent.putExtra("position", StaticList.booklist.get(position).getPosition());
                 intent.putExtra("id",StaticList.booklist.get(position).getBookId());
				 startActivity(intent);
				
			}
		});
    }
    public void gaindir(int listposition) throws IOException{

        StaticList.bookdirlist.clear();
        String path = StaticList.booklist.get(listposition).getBookPath();
        GetTextCode(path);
        FileInputStream fInputStream = new FileInputStream(new File(path));
        InputStreamReader inputStreamReader = new InputStreamReader(fInputStream, Setting_text_code);
        BufferedReader in = new BufferedReader(inputStreamReader);
        String line =  in.readLine();
        long position = 0;
        int lengthArr;
        String temp;
        while(line!= null){
            temp = line.trim();
            lengthArr = temp.length();
            if (lengthArr > 20)
            {
                temp = temp.substring(0, 20);
            }
            else {
                temp = temp.substring(0, lengthArr);
            }
            if (temp.contains("第") && (temp.contains("章") || temp.contains("节")))
            {
                BookDir bDir = new BookDir(position, temp,StaticList.booklist.get(listposition).getBookId());
                StaticList.bookdirlist.add(bDir);
            }

            position += line.length()+2;
            line = in.readLine();
        }
        in.close();
    }
    private void GetTextCode(String path)
    {

        try{
            FileInputStream fileIS = new FileInputStream(new File(path));
            BufferedInputStream buf = new BufferedInputStream(fileIS);
            buf.mark(4);
            byte[] first3bytes = new byte[3];
            buf.read(first3bytes);
            buf.reset();
            if(first3bytes[0] == (byte)0xEF && first3bytes[1] == (byte)0xBB && first3bytes[2] == (byte)0xBF) {
                Setting_text_code = "utf-8";
            }else if(first3bytes[0] == (byte)0xFF && first3bytes[1] == (byte)0xFE) {
                Setting_text_code = "unicode";
            }else if(first3bytes[0] == (byte)0xFE && first3bytes[1] == (byte)0xFF) {
                Setting_text_code = "utf-16be";
            }else {
                Setting_text_code = "GBK";
            }
            System.out.println("编码格式：" + Setting_text_code);
            buf.close();
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

//        boolean isDrawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

//        menu.findItem(R.id.action_websearch).setVisible( !isDrawerOpen );
        return super.onPrepareOptionsMenu(menu);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.myshelf, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if(mDrawerToggle.onOptionsItemSelected(item)){

            return true;
        }

        switch(item.getItemId()) {
            case R.id.importbooks:
                Intent intent=new Intent(this,ScanningActivity.class);
                startActivity(intent);
                break;
            case R.id.bookonline:
    //	    	Intent intent1=new Intent(this,BookOnlineActivity.class);
    //	     	startActivity(intent1);
                Toast.makeText(this,"bookonline",Toast.LENGTH_SHORT).show();
                break;
	    	
	    default:
     		return super.onOptionsItemSelected(item);
	    	}
	    return true;
    }

    public class MultiChoiceModeListener implements GridView.MultiChoiceModeListener { 
		private static final int MENU_SELECT_ALL = 0;
	    private Context mContext;
	    public MultiChoiceModeListener(Context context){
	    	mContext = context;
	    }
	    
	    @Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//			View v = LayoutInflater.from(mContext).inflate(R.layout.actionbar_layout,
//	                null);
//	        mode.setCustomView(v);  
	        getMenuInflater().inflate(R.menu.action_menu, menu);  
	        return true; 
		}
	    
	    @Override
		public void onDestroyActionMode(ActionMode arg0) {
			adapter=new MyshelfAdapter();
	        bookShelf.setAdapter(adapter);

		
		}
	    
		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu menu) {
			// TODO Auto-generated method stub
			menu.getItem(MENU_SELECT_ALL).setEnabled(
	                bookShelf.getCheckedItemCount() != bookShelf.getCount());
	        return true;
		}
		
		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position, long arg2,
				boolean checked) {
            isSelected.put(position, !isSelected.get(position));
			adapter.notifyDataSetChanged();
	        //mode.invalidate();
		}
		
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        // TODO Auto-generated method stub
	    	switch (item.getItemId()) {
	    	case R.id.menu_select:
	    		for (int i = 0; i < bookShelf.getCount(); i++) {
	    			bookShelf.setItemChecked(i, true);
                    isSelected.put(i, true);
	    		}
	    		break;
	    	case R.id.menu_delete:
	    		for(int i = 0;i<bookShelf.getCount();i++){
	    			if(isSelected.get(i)){
	    				bService.delete(StaticList.booklist.get(i).getBookId());
	    				bService.deleteBookMark(StaticList.booklist.get(i).getBookId());
	    				
	    			}
	    					
	    		}
	    		adapter = new MyshelfAdapter();
	    		bookShelf.setAdapter(adapter);
	    		break;
		    	}
	        return true;
	    }
	
    }

    private class MyshelfAdapter extends BaseAdapter {


        private LayoutInflater layoutInflater;


        @SuppressLint("UseSparseArrays")
        public MyshelfAdapter() {
            super();
            StaticList.booklist = bService.getScrollData(0, (int)bService.getCount());
            for(int i = 0;i<StaticList.booklist.size();i++){
                isSelected.put(i, false);
            }
            layoutInflater = LayoutInflater.from(mContext);
        }


        public int getCount() {
            if (null != StaticList.booklist)
            {
                return StaticList.booklist.size();
            }
            else
            {
                return 0;
            }
        }

        public Object getItem(int position ) {
            return StaticList.booklist.get(position);
        }

        public long getItemId(int position ) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent ) {
            ViewHolder vHolder;
            if (convertView == null)
            {
                vHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.item_shelfgrid, null);
                vHolder.tv = (TextView)convertView.findViewById(R.id.textview2);
                vHolder.iv = (ImageView)convertView.findViewById(R.id.select);
                convertView.setTag(vHolder);
            } else
            {
                vHolder = (ViewHolder) convertView.getTag();
            }
            vHolder.tv.setText(StaticList.booklist.get(position).getBookName());
            vHolder.iv.setVisibility(isSelected.get(position) ? View.VISIBLE : View.GONE);
            return convertView;
        }


    }
    private static class ViewHolder {

        public TextView tv;
        public ImageView iv;
    }


}
