package com.example.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.Book;
import com.example.entity.BookMark;
import com.example.entity.MenuInfo;
import com.example.reader.R;
import com.example.service.BookService;
import com.example.service.MyTextview;
import com.example.service.PreferencesService;
import com.example.service.StaticList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@SuppressLint({ "SimpleDateFormat", "ResourceAsColor" })
public class TxtViewerActivity extends Activity implements OnGestureListener ,FlipperLayout.TouchListener{
    /**
     * 遗留问题，color不会存入preference，所以这个写死了，阅读背景不能自定义换
     */
    private final String TAG = "TxtViewerActivity";
	
	private String fileName;
	private GestureDetector mGestureDetector;
	private long mTotalSkipBytes = 0;
	private BookService bService;
	private PreferencesService pService;
	private PopupWindow popup,popupfontsize;
	private MenuAdapter menuAdapter;
	private List<MenuInfo> menulists;
	private GridView menuGridView;
	private LinearLayout linearLayout1;
	private Vector<String> mVector = null;
	private int mScreenWidth = 0;
    private Integer fontcolor = Color.BLACK;
    private Integer bgColorRes = Color.WHITE;
    private Integer fontsize = 30;
    private int bookid;
    private Boolean nightmodel = false;
	private TextView tView1;
    private TextView tView2;
    private Context mContext = this;
    private MyTextview myTextview;
	private String Setting_text_code;
    private FlipperLayout rootLayout;

    private View on,show,down;
    private MyTextview onmytextview,showmytextview,downmytextview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initPopuWindows();

		DisplayMetrics dm = getResources().getDisplayMetrics();
	  	mScreenWidth = dm.widthPixels;

        bService = new BookService(this);
        pService = new PreferencesService(this);

        Intent intent=getIntent();
        fileName=intent.getStringExtra("path");
        mTotalSkipBytes = intent.getLongExtra("position", 0);
        bookid = intent.getIntExtra("id", 0);

        setContentView(R.layout.activity_txt_viewer);

        rootLayout = (FlipperLayout) findViewById(R.id.slidepage_container);
        on = LayoutInflater.from(this).inflate(R.layout.item_slidepage, null);
        onmytextview = (MyTextview) on.findViewById(R.id.slidepage_mytextview);
        initMyTextView(onmytextview,mTotalSkipBytes,-1);

        show = LayoutInflater.from(this).inflate(R.layout.item_slidepage, null);
        showmytextview = (MyTextview) show.findViewById(R.id.slidepage_mytextview);
        initMyTextView(showmytextview,mTotalSkipBytes,1);

        down = LayoutInflater.from(this).inflate(R.layout.item_slidepage, null);
        downmytextview = (MyTextview) down.findViewById(R.id.slidepage_mytextview);
        initMyTextView(downmytextview,mTotalSkipBytes,2);

        rootLayout.initFlipperViews(this,down,show,on);


//        ViewTreeObserver vto2 = textView_reader.getViewTreeObserver();
//        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                textView_reader.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                Log.i("TAG","宽："+textView_reader.getWidth()+"高："+textView_reader.getHeight());
//            }
//        });

        mGestureDetector = new GestureDetector(TxtViewerActivity.this,this);

    }
    private void initMyTextView(MyTextview myTextview,long mTotalSkipBytes,int mode){
        GetTextCode();
        final Bundle bundle = new Bundle();
        bundle.putString("filename",fileName);
        bundle.putString("textcode",Setting_text_code);
        bundle.putLong("skipbytes",mTotalSkipBytes);
        bundle.putInt("mode",mode);
        LoadSettings();
        if(nightmodel){
            fontcolor = getResources().getColor(R.color.light);
            this.getWindow().setBackgroundDrawableResource(R.color.dark);
        }else{
            fontcolor = Color.BLACK;
            this.getWindow().setBackgroundDrawableResource(R.color.light);
        }
        bundle.putInt("fontcolor",fontcolor);
        bundle.putInt("fontsize",fontsize);
        myTextview.setBundle(bundle);
    }

    private void LoadSettings()
    {
        Map<String, Object> params = pService.getPreferences();
        fontsize = (Integer)params.get("textsize");
        fontcolor = (Integer)params.get("textcolor");
        bgColorRes = (Integer)params.get("bgcolor");
        nightmodel = (Boolean)params.get("nightmodel");
    }

	protected void onPause(){
        pService.save(fontsize,fontcolor,nightmodel);
		long saveposition = showmytextview.getmTotalSkipBytes();
		bService = new BookService(this);
		Book book = new Book(bookid,fileName,saveposition);
		bService.updatePosition(book);
		super.onPause();
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
	{
    	if (keyCode == KeyEvent.KEYCODE_BACK )
    	{
    		finish();
			return true;
    	}
    	return false;
	}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        fileName=intent.getStringExtra("path");
        mTotalSkipBytes = intent.getLongExtra("position", 0);
        bookid = intent.getIntExtra("id", 0);
        myTextview.setmTotalSkipBytes(mTotalSkipBytes);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return true;
	}
	
	 @Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		if (popup != null) {
			menulists = MenuUtils.getTxtViewerMenu();
			menuAdapter = new MenuAdapter();
			menuGridView.setAdapter(menuAdapter);
			popup.showAtLocation(this.findViewById(R.id.slidepage_container), Gravity.BOTTOM, 0, 0);
		}
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("menu");
		return super.onPrepareOptionsMenu(menu);
	}

    /**
     *两个弹窗
     */
    private void initPopuWindows() {
            /**
             * 字体增加减小的弹窗
             */
		    linearLayout1 = (LinearLayout)View.inflate(this, R.layout.popup_textsize, null);
		    tView1 = (TextView)linearLayout1.findViewById(R.id.textsize_textView1);
		    tView2 = (TextView)linearLayout1.findViewById(R.id.textsize_textView2);
		    popupfontsize = new PopupWindow(linearLayout1, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
		    popupfontsize.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_background));
			popupfontsize.setFocusable(true);
			popupfontsize.setAnimationStyle(R.style.menushow);
			popupfontsize.update();
			
			linearLayout1.setFocusableInTouchMode(true);
			tView1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
                    /** 减小字体 **/
					fontsize -= 1;
                    myTextview.setFontsize(fontsize);
				}
			});
			tView2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					fontsize += 1;
                    myTextview.setFontsize(fontsize);
				}
			});


            /**
             * 菜单弹窗
             */
			menuGridView=(GridView)View.inflate(this, R.layout.gridview_menu, null);
            menuGridView.setOverScrollMode(View.OVER_SCROLL_NEVER);
			popup = new PopupWindow(menuGridView, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_background));
			popup.setFocusable(true);
			popup.setAnimationStyle(R.style.menushow);
			popup.update();
			menuGridView.setFocusableInTouchMode(true);
			menuGridView.setOnKeyListener(new android.view.View.OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if ((keyCode == KeyEvent.KEYCODE_MENU) && (popup.isShowing())) {
						popup.dismiss();
						return true;
						
					}
					return false;
				}

			});
			menuGridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					MenuInfo mInfo = menulists.get(arg2);
					popup.dismiss();
					switch (mInfo.menuId) {
					
					case MenuUtils.MENU_NIGHTMODE:
                        LoadSettings();
                        nightmodel = !nightmodel;
                        if(nightmodel){
                            fontcolor = getResources().getColor(R.color.light);
                            TxtViewerActivity.this.getWindow().setBackgroundDrawableResource(R.color.dark);
                        }else{
                            fontcolor = Color.BLACK;
                            TxtViewerActivity.this.getWindow().setBackgroundDrawableResource(R.color.light);
                        }
                        myTextview.setFontcolor(fontcolor);
						pService.save(fontsize, fontcolor, nightmodel);
						break;
					case MenuUtils.MENU_SKIP:
						break;
					case MenuUtils.MENU_FONT:
						if (popupfontsize != null) {
							popupfontsize.showAtLocation(
									TxtViewerActivity.this.findViewById(R.id.slidepage_container),
									Gravity.BOTTOM, 0, 0);
						}
						break;
					case MenuUtils.MENU_LANDSCAPEMODE:
						break;
					case MenuUtils.MENU_ADD_BOOKMARK:
						SimpleDateFormat   formatter   =   new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
					    Date   curDate   =   new   Date(System.currentTimeMillis());    
						String   datetime   =   formatter.format(curDate); 
						mVector = myTextview.getmText();
                        String content = "";
                        for(String v:mVector){
                            content += v;
                        }
						BookMark bMark = new BookMark(myTextview.getmTotalSkipBytes(),
								datetime,content,bookid);
						bService.addBookMark(bMark);
						Toast.makeText(TxtViewerActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
						break;
					case MenuUtils.MENU_DIRECTORY_BOOKMARK:	
//						Intent intent = new Intent(TxtViewerActivity.this,BookMarkActivity.class);
                        Intent intent = new Intent(TxtViewerActivity.this,BookMarkDirActivity.class);
			            intent.putExtra("bookid",bookid);
						startActivity(intent);
						break;
					case MenuUtils.MENU_OTHER:
						break;
					case MenuUtils.MENU_EXIT:	
						Intent intent2 = new Intent(TxtViewerActivity.this,MyshelfActivity.class);
						intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						TxtViewerActivity.this.startActivity(intent2);
						break;
					}
				}
			});
		}


	
	private void GetTextCode()
	{

		try{
			FileInputStream fileIS = new FileInputStream(new File(fileName));
			BufferedInputStream buf = new BufferedInputStream(fileIS);
			buf.mark(4);
			byte[] first3bytes = new byte[3];
			buf.read(first3bytes);//
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
			buf.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}
	
	@Override
	public boolean onDown(MotionEvent arg0) {
//		// TODO Auto-generated method stub
//		if(arg0.getX()<(mScreenWidth/3)) {
//			myTextview.pageup();
//		}else if(arg0.getX()>(mScreenWidth/3*2)){
//			myTextview.pagedown();
//		}else{
//            if (popup != null) {
//                menulists = MenuUtils.getTxtViewerMenu();
//                menuAdapter = new MenuAdapter();
//                menuGridView.setAdapter(menuAdapter);
//                popup.showAtLocation(this.findViewById(R.id.slidepage_container), Gravity.BOTTOM, 0, 0);
//            }
//        }
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
//				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
//			// ������໬����ʱ�� //����View������Ļʱ��ʹ�õĶ���
//			//mFlipper.setInAnimation(inFromRightAnimation());
//			// ����View�˳���Ļʱ��ʹ�õĶ���
//		//mFlipper.setOutAnimation(outToLeftAnimation());
//			//mFlipper.showNext();
//			if(ReadBytes!=-1)
//			{
//				TotalSkipBytes=TotalSkipBytes+CurrentByteInPage;
//				textView_reader.setText(getStringFromFileForward(TotalSkipBytes));
//				updatePageNum();
//				//System.out.println("CurrentByteInPage is "+CurrentByteInPage);
//				//System.out.println("skipnumber is in getStringFromFile"+TotalSkipBytes);
//				//System.out.println("**************Slide to left************");
//			}
//		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
//				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
//
//			// �����Ҳ໬����ʱ��
//		//	mFlipper.setInAnimation(inFromLeftAnimation());
//			//mFlipper.setOutAnimation(outToRightAnimation());
//		//	mFlipper.showPrevious();
//			textView_reader.setText(getStringFromFileBackwards(TotalSkipBytes));
//			TotalSkipBytes=TotalSkipBytes-CurrentByteInPage;
//			if(TotalSkipBytes<0)
//				TotalSkipBytes=0;
//			updatePageNum();
//			//System.out.println("CurrentByteInPage is "+CurrentByteInPage);
//			//System.out.println("skipnumber is in getStringFromFile"+TotalSkipBytes);
//			//System.out.println("**************Slide to right************");
//		}
		
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {		}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {		return false;
	}

    @Override
    public View createView(int direction) {
        switch (direction){
            case -1:
                mTotalSkipBytes = mTotalSkipBytes + downmytextview.getmCurrentByteInPage();
                down = LayoutInflater.from(this).inflate(R.layout.item_slidepage, null);
                downmytextview = (MyTextview) down.findViewById(R.id.slidepage_mytextview);
                initMyTextView(downmytextview,mTotalSkipBytes,1);
                return down;

            case 1:
                mTotalSkipBytes = mTotalSkipBytes - onmytextview.getmCurrentByteInPage();
                on = LayoutInflater.from(this).inflate(R.layout.item_slidepage, null);
                onmytextview = (MyTextview) on.findViewById(R.id.slidepage_mytextview);
                initMyTextView(onmytextview,mTotalSkipBytes,-1);
                return on;
            default:
                return null;
        }
    }

    @Override
    public boolean currentIsFirstPage() {
        if(mTotalSkipBytes == 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean currentIsLastPage() {
        boolean tureralse = downmytextview.getIsEnd();
        return tureralse;
    }

    @Override
    public boolean whetherHasPreviousPage() {
        if(mTotalSkipBytes == 0){
            return false;
        }
        return true;
    }

    @Override
    public boolean whetherHasNextPage() {
        boolean tureralse = downmytextview.getIsEnd();
        return !tureralse;
    }


    private class MenuAdapter extends BaseAdapter {

        private final LayoutInflater inflater;
        public MenuAdapter(){
            inflater=LayoutInflater.from(mContext);
        }

        public int getCount() {
            return menulists.size();
        }

        public Object getItem(int arg0) {
            return menulists.get(arg0);
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public View getView(int arg0, View convertView, ViewGroup arg2) {
            ViewHodler viewHodler;
            MenuInfo mInfo=menulists.get(arg0);
            if (convertView==null) {
                viewHodler = new ViewHodler();
                convertView = inflater.inflate(R.layout.item_menu, null);
                viewHodler.imageView = (ImageView)convertView.findViewById(R.id.item_image);
                viewHodler.textView = (TextView)convertView.findViewById(R.id.item_text);
                convertView.setTag(viewHodler);
            }else{
                viewHodler = (ViewHodler)convertView.getTag();
            }
            viewHodler.imageView.setImageResource(mInfo.imgsrc);
            viewHodler.textView.setText(mInfo.title);
//		if (mInfo.ishide) {
//			iView.setAlpha(80);
//		}
            return convertView;
        }

    }
    private static class ViewHodler{
        public ImageView imageView;
        public TextView textView;
    }
}
