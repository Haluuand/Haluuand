package com.example.activity;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.entity.Book;
import com.example.entity.BookMark;
import com.example.reader.R;
import com.example.service.BookService;
import com.example.service.StaticList;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by admin on 2015/4/5.
 */
public class BookMarkFragment extends Fragment{

    private ListView listView;
    private int bookid;
    private Adapter adapter;
    private BookService bService;
    private List<BookMark> bookMarks;

    public static BookMarkFragment newInstance(int bookid) {
        BookMarkFragment f = new BookMarkFragment();
        Bundle b = new Bundle();
        b.putInt("bookid", bookid);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bookid = getArguments().getInt("bookid");
        bService = new BookService(getActivity());
        bookMarks = bService.getBookMark(bookid);

        listView = new ListView(getActivity());
        registerForContextMenu(listView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        if(bookMarks.size() == 0) {

            FrameLayout fl = new FrameLayout(getActivity());
            fl.setLayoutParams(params);

            final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                    .getDisplayMetrics());

            TextView v = new TextView(getActivity());
            params.setMargins(margin, margin, margin, margin);
            v.setLayoutParams(params);
            v.setLayoutParams(params);
            v.setGravity(Gravity.CENTER);
            v.setTextSize(30);
            v.setText("还没有添加书签哦");

            fl.addView(v);
            return fl;
        }

        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setLayoutParams(params);
        listView.setItemsCanFocus(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Book book = bService.getBookById(bookid);
                Intent intent = new Intent(getActivity(),TxtViewerActivity.class);
                intent.putExtra("position",bookMarks.get(position).getPosition());
                intent.putExtra("path", book.getBookPath());
                intent.putExtra("id",book.getBookId());
                startActivity(intent);

                getActivity().finish();

            }
        });

        return listView;
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater mInflater = getActivity().getMenuInflater();
        mInflater.inflate(R.menu.bookmark_contextmenu, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = itemInfo.position;
        //
        switch(item.getItemId()){
            case R.id.bookmark_goto:
                Book book = bService.getBookById(bookid);
                System.out.println(book.toString());
                Intent intent = new Intent(getActivity(),TxtViewerActivity.class);
                intent.putExtra("position",bookMarks.get(position).getPosition());
                intent.putExtra("path", book.getBookPath());
                intent.putExtra("id",bookid);
                startActivity(intent);
                break;
            case R.id.bookmark_delete:
                bService.deleteBookMark(bookMarks.get(position).getBookmarkId());
                bookMarks = bService.getBookMark(bookid);
                adapter = new Adapter();
                listView.setAdapter(adapter);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private class Adapter extends BaseAdapter{

        private LayoutInflater inflater = LayoutInflater.from(getActivity());
        public Adapter(){
            super();
        }
        @Override
        public int getCount() {
            return bookMarks.size();
        }

        @Override
        public Object getItem(int position) {
            return bookMarks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

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
    private static class ViewHolder{
        public TextView tv1;
        public TextView tv2;
    }
}
