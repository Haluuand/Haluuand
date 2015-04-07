package com.example.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.entity.Book;
import com.example.reader.R;
import com.example.service.BookService;
import com.example.service.StaticList;

/**
 * Created by admin on 2015/4/5.
 */
public class BookMarkDirActivity extends FragmentActivity{

    private String Tag = "BookMarkDirActivity";

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private TextView textView;
    private int bookid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 设置屏幕全屏
         */
        final Window win = getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_bookmark_dir);
        Intent intent=getIntent();
        bookid = intent.getIntExtra("bookid", 0);

        BookService bookService = new BookService(this);
        Book book = bookService.getBookById(bookid);

        textView = (TextView)findViewById(R.id.bookmark_dir_textview);
        textView.setText(book.getBookName());

        tabs = (PagerSlidingTabStrip) findViewById(R.id.bookmark_dir_tabs);
        pager = (ViewPager) findViewById(R.id.bookmark_dir_pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "目录", "书签"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return BookDirFragment.newInstance(bookid);
                case 1:
                    return BookMarkFragment.newInstance(bookid);
                default:
                    return SuperAwesomeCardFragment.newInstance(position);
            }

        }

    }

}
