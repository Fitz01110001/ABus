package com.wind.fitz.slideviewdemo.fitzview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * @ProjectName: ABus
 * @Package: com.wind.fitz.slideviewdemo.fitzview
 * @Author: Fitz
 * @CreateDate: 2019/1/10
 */
public class FitzOnItemTouchListener implements RecyclerView.OnItemTouchListener {

    public FitzOnItemTouchListener(Context context) {
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
