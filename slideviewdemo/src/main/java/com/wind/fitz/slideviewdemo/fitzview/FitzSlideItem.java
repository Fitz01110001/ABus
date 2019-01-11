package com.wind.fitz.slideviewdemo.fitzview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.fitzview
 * @Author: Fitz
 * @CreateDate: 2019/1/8
 */
public class FitzSlideItem extends RelativeLayout {

    private static final String TAG = "FitzSlideItem";


    public FitzSlideItem(Context context) {
        super(context);
    }

    public FitzSlideItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitzSlideItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FitzSlideItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        FLOG("widthMeasureSpec:" + widthMeasureSpec + " heightMeasureSpec:" + heightMeasureSpec);

    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
        FLOG("measureChild");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void FLOG(String s) {
        Log.d(TAG, s);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onInterceptTouchEvent  ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onInterceptTouchEvent  ACTION_MOVE:");
                break;

                default:break;
        }
        return false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent  ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent  ACTION_MOVE:");
                break;

            default:break;
        }

        return super.onTouchEvent(event);
    }
}
