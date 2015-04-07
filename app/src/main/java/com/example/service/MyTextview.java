package com.example.service;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.reader.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by bwk1217 on 2015/4/3.
 */
public class MyTextview extends View{

    private static final String TAG = "MyTextview";

    /** 可以通过set设置的属性 */
    private int fontsize = 30;
    private int fontcolor = Color.BLACK;
    private int strokesize = 5;
    private Typeface typeface = Typeface.DEFAULT;

    /** 此控件宽高 */
    private int viewwidth;
    private int viewheight;

    /** 需要传进来 */
    private String mFileName = "";
    private String mTextCode = "";
    private long mTotalSkipBytes = 0;

    private TextPaint textPaint;
    private int mFontHeight = 0;
    private int mPageLineNum = 0;
    private float mLineOffset = 0;
    public  static int mTotalTextHeight = 0;
    private int mCurrentByteInPage=0;
    private int mMaxByteInPage=0;
    private Context mContext;
    private Vector<String> mText = null;
    private int mFileEnd = 0;
    private File file;
    private double filesize;
    private double f;

    public MyTextview(Context context) {
        this(context, null);
    }

    public MyTextview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }
    public void setBundle(Bundle bundle){
        mFileName = bundle.getString("filename");
        mTextCode = bundle.getString("textcode");
        mTotalSkipBytes = bundle.getLong("skipbytes", 0);
        fontcolor = bundle.getInt("fontcolor");
        fontsize = bundle.getInt("fontsize");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewwidth = MeasureSpec.getSize(widthMeasureSpec);
        viewheight = MeasureSpec.getSize(heightMeasureSpec);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textPaint.setStrokeWidth(strokesize);
        textPaint.setColor(fontcolor);
        textPaint.setTextSize(fontsize);
        textPaint.setTypeface(typeface);

        Paint.FontMetrics fm = textPaint.getFontMetrics();
        mFontHeight = (int) Math.ceil(fm.bottom - fm.top);
        mPageLineNum = viewheight / mFontHeight;
        mLineOffset = (viewheight % mFontHeight) / (float)mPageLineNum;

        /**
         * 获取屏幕能容纳的最大字符数
         */
        float[] widths = new float[1];
        String srt = "i";
        textPaint.getTextWidths(srt, widths);
        int widt = (int) (Math.ceil(widths[0]));
        mMaxByteInPage = viewwidth/widt*mPageLineNum;

        int x=0;
        float y = -(fm.top);

        mText = getStringFromFileForward(mTotalSkipBytes);
        for (int i = 0, j = 0; i < mText.size(); i++, j++)
        {
            canvas.drawText(mText.elementAt(i), x, y + ((mFontHeight + mLineOffset) * j), textPaint);
        }
    }

    /**
     * 读取下一页显示的字符串
     * @param bytenumber 跳过的字符
     * @return 以行为单位的字符串集，用vector因为是安全的单线程
     */
    private Vector<String> getStringFromFileForward(long bytenumber) {

        Vector<String> mString = new Vector<String>();
        char buff[] = new char[mMaxByteInPage];
        char ch;
        int w = 0;
        int istart = 0;
        int real_line = 0;
        int i=0;

        try {
            FileInputStream fInputStream = new FileInputStream(new File(mFileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fInputStream, mTextCode);
            BufferedReader in = new BufferedReader(inputStreamReader);
            in.skip(bytenumber);
            mFileEnd = in.read(buff, 0, mMaxByteInPage);
            String string_temp = new String(buff);

            if(mFileEnd == -1){
                in.close();
                return null;
            }else{
                for(i=0; i < mFileEnd; i++) {
                    ch = buff[i];
                    float[] widths = new float[1];
                    String srt = String.valueOf(ch);
                    textPaint.getTextWidths(srt, widths);
                    if (ch == '\n')
                    {
                        real_line++;
                        mString.addElement(string_temp.substring(istart, i));
                        istart = i + 1;
                        w = 0;
                    }
                    else
                    {
                        w += (int) (Math.ceil(widths[0]));
                        if (w > viewwidth)
                        {
                            real_line++;
                            mString.addElement(string_temp.substring(istart, i));
                            istart = i;
                            i--;
                            w = 0;
                        }else
                        {
                            if (i == mFileEnd-1)
                            {
                                real_line++;
                                mString.addElement(string_temp.substring(istart, mFileEnd));
                            }
                        }
                    }

                    if((real_line+1) > mPageLineNum) break;
                }
                in.close();
                mCurrentByteInPage = i+1;
                mTotalTextHeight=(int)(real_line * (mFontHeight + mLineOffset)+2);
                return mString;
            }

        } catch (Exception e) {
            System.out.println("MyTextview Error at 319.");
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 读取上一页显示的字符串
     * @param bytenumber 跳过的字符
     * @return 以行为单位的字符串集，用vector因为是安全的单线程
     */
    private Vector<String> getStringFromFileBackward(long bytenumber) {
        Vector<String> mString = new Vector<String>();
        Vector<String> mbackString = new Vector<String>();
        Vector<Integer> mbyte = new Vector<Integer>();

        long mbytenumber = bytenumber;
        char buff[] = new char[mMaxByteInPage];
        char ch;
        int w = 0;
        int istart = 0;
        int real_line = 0;
        int i=0;
        try {
            FileInputStream fInputStream = new FileInputStream(new File(mFileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fInputStream, mTextCode);
            BufferedReader in = new BufferedReader(inputStreamReader);

            mbytenumber -= mMaxByteInPage;
            if(mbytenumber < 0) {
                mbytenumber = 0;
            }
            in.skip(mbytenumber);
            buff = new char[mMaxByteInPage];
            if(mbytenumber == 0) {

                mFileEnd = in.read(buff, 0, (int)bytenumber);
                String string_temp = new String(buff);
                for(i=0; i < mFileEnd; i++) {
                    ch = buff[i];
                    float[] widths = new float[1];
                    String srt = String.valueOf(ch);
                    textPaint.getTextWidths(srt, widths);
                    if (ch == '\n')
                    {
                        real_line++;
                        mString.addElement(string_temp.substring(istart, i));
                        istart = i + 1;
                        mbyte.add(i);
                        w = 0;
                    }
                    else
                    {
                        w += (int) (Math.ceil(widths[0]));
                        if (w > viewwidth)
                        {
                            real_line++;
                            mString.addElement(string_temp.substring(istart, i));
                            istart = i;
                            i--;
                            mbyte.add(i);
                            w = 0;
                        }
                        else
                        {
                            if (i == mFileEnd-1)
                            {
                                real_line++;
                                mString.addElement(string_temp.substring(istart, mFileEnd));
                            }
                        }
                    }

                }
                if(real_line == mPageLineNum){
                    mbackString = mString;
                    i=mFileEnd;
                }else{
                    int j = real_line - mPageLineNum;
                    i=(mFileEnd-2) -mbyte.get(j-1);
                    for(;j<real_line;j++){
                        mbackString.addElement(mString.get(j));
                    }

                }
            } else {
                mFileEnd = in.read(buff, 0, mMaxByteInPage);
                String string_temp = new String(buff);
                for(i=0; i < mMaxByteInPage; i++) {
                    ch = buff[i];
                    float[] widths = new float[1];
                    String srt = String.valueOf(ch);
                    textPaint.getTextWidths(srt, widths);
                    if (ch == '\n')
                    {
                        real_line++;
                        mString.addElement(string_temp.substring(istart, i));
                        istart = i + 1;
                        mbyte.add(i);
                        w = 0;
                    }
                    else
                    {
                        w += (int) (Math.ceil(widths[0]));
                        if (w > viewwidth)
                        {
                            real_line++;
                            mString.addElement(string_temp.substring(istart, i));
                            istart = i;
                            i--;
                            mbyte.add(i);
                            w = 0;
                        }
                        else
                        {
                            if (i == mMaxByteInPage-1)
                            {
                                real_line++;
                                mString.addElement(string_temp.substring(istart, mMaxByteInPage));
                            }
                        }
                    }

                }//else��forѭ���Ľ���

                int j = real_line - mPageLineNum;
                i=(mMaxByteInPage-2) -mbyte.get(j-1);


                for(;j<real_line;j++){
                    mbackString.addElement(mString.get(j));
                }
            }
            in.close();
            mCurrentByteInPage = i+1;
            mTotalTextHeight=(int)(real_line * (mFontHeight + mLineOffset)+2);
            return mbackString;
        }
        catch (Exception e) {
            System.out.println("Java Error!!!!!!!!!!");
            e.printStackTrace();
        }

        return null;
    }

    public void pageup(){
        file = new File(mFileName);
        filesize = file.length();
        if(mTotalSkipBytes == 0){
            Toast.makeText(mContext, "第一页！", Toast.LENGTH_SHORT).show();

        }else{
            setText(getStringFromFileBackward(mTotalSkipBytes));
            mTotalSkipBytes = mTotalSkipBytes - mCurrentByteInPage;
            if(mTotalSkipBytes<0)
                mTotalSkipBytes=0;
        }
        f = mTotalSkipBytes/filesize;
        System.out.println(f);
    }

    public void pagedown(){
        file = new File(mFileName);
        filesize = file.length();
        mTotalSkipBytes = mTotalSkipBytes + mCurrentByteInPage;
        Vector<String> vjudge = getStringFromFileForward(mTotalSkipBytes);
        if(mFileEnd != -1) {
            setText(vjudge);
        }else{
            mTotalSkipBytes = mTotalSkipBytes - mCurrentByteInPage;
            Toast.makeText(mContext,"最后一页！",Toast.LENGTH_SHORT).show();
        }
        f = mTotalSkipBytes/filesize;
        System.out.println(f);
    }

    public void setmTotalSkipBytes(long mTotalSkipBytes) {
        this.mTotalSkipBytes = mTotalSkipBytes;
        invalidate();
    }

    public void setFontsize(int fontsize) {
        this.fontsize = fontsize;
        invalidate();
    }

    public void setFontcolor(int fontcolor) {
        this.fontcolor = fontcolor;
        invalidate();
    }

    public void setStrokesize(int strokesize) {
        this.strokesize = strokesize;
        invalidate();
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        invalidate();
    }

    public Vector<String> getmText() {
        return mText;
    }

    public long getmTotalSkipBytes() {
        return mTotalSkipBytes;
    }

    private void setText(Vector<String> text) {
        mText = text;
        invalidate();
    }
}
