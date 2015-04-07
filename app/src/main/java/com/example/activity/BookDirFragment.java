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

import java.util.List;

/**
 * Created by admin on 2015/4/5.
 */
public class BookDirFragment extends Fragment{

    private ListView listView;
    private int bookid;
    private MyAdapter adapter;
    private BookService bService;
    private List<BookMark> bookMarks;

    public static BookDirFragment newInstance(int bookid) {
        BookDirFragment f = new BookDirFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        if(StaticList.bookdirlist.size() == 0) {

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
            v.setText("没有检测到目录");

            fl.addView(v);
            return fl;
        }

        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.setLayoutParams(params);
        listView.setItemsCanFocus(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Book book = bService.getBookById(bookid);
                Intent intent = new Intent(getActivity(),TxtViewerActivity.class);
                intent.putExtra("position", StaticList.bookdirlist.get(position).getPosition());
                intent.putExtra("path", book.getBookPath());
                intent.putExtra("id",book.getBookId());
                startActivity(intent);

                getActivity().finish();

            }
        });
        return listView;
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater = LayoutInflater.from(getActivity());

        @Override
        public int getCount() {
            return StaticList.bookdirlist.size();
        }

        @Override
        public Object getItem(int position) {
            return StaticList.bookdirlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView textView;

            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_bookdir, null);
                textView =
                        (TextView) convertView.findViewById(R.id.bookdir_listview);
                convertView.setTag(textView);
            } else {
                textView = (TextView) convertView.getTag();
            }
            textView.setText(StaticList.bookdirlist.get(position).getDirname());
            return convertView;
        }
    }
}
