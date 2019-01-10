package com.wind.fitz.slideviewdemo.fitzview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.wind.fitz.slideviewdemo.adapter.FragmentListAdapter;

/**
 * @ProjectName: ABus
 * @Package: com.wind.fitz.slideviewdemo.fitzview
 * @Author: Fitz
 * @CreateDate: 2019/1/10
 */
public class FitzOnItemTouchListener implements RecyclerView.OnItemTouchListener {

    private static final int TOUCH_STATE_NONE = 0; //按下状态
    private static final int TOUCH_STATE_X = 1;//横滑状态
    private static final int TOUCH_STATE_Y = 2;//竖滑状态
    //判断横竖滑动的最小值
    private static final int MAX_Y = 5;
    private static final int MAX_X = 3;
    private FitzSlideItem mTouchView = null;//记录当前点击的item View
    private float mDownX;//x轴坐标
    private float mDownY;//y轴坐标
    private int mTouchState;//记录点击状态
    private FragmentListAdapter.FitzViewHolder curHolder;
    private FragmentListAdapter.FitzViewHolder oldHolder;

    public FitzOnItemTouchListener(Context context) {
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        if (motionEvent.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null) {
            return false;
        }

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取当前的item的View
                View currentView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (currentView != null) {

                } else {

                }
                //记录位置
                mDownX = motionEvent.getX();
                mDownY = motionEvent.getY();
                mTouchState = TOUCH_STATE_NONE;
                //根据当前横纵坐标点获取点击的item的position

                curHolder = (FragmentListAdapter.FitzViewHolder) recyclerView.getChildViewHolder(currentView);

                //判断当前点击的是否和上次点击的item是同一个，如果是同一个，并且状态是打开了的就记录状态和坐标
                //记录坐标通过Item中的downX属性
                if (curHolder != oldHolder && mTouchView != null && mTouchView.isOpen()) {
                    mTouchState = TOUCH_STATE_X;
                    mTouchView.onSwipe(motionEvent);
                    return true;
                }

                //如果不是同一个item 那么点击的话就关闭掉之前打开的item
                if (mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                    mTouchView = null;
                    return false;
                }
                //判断该view的类型
                if (currentView instanceof FitzSlideItem) {
                    mTouchView = (FitzSlideItem) currentView;
                }
                if (mTouchView != null) {
                    mTouchView.onSwipe(motionEvent);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = Math.abs((motionEvent.getY() - mDownY));
                float dx = Math.abs((motionEvent.getX() - mDownX));
                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        //执行滑动
                        mTouchView.onSwipe(motionEvent);
                    }
                    return true;
                } else if (mTouchState == TOUCH_STATE_NONE) {
                    //判断滑动方向，x方向执行滑动，Y方向执行滚动
                    if (Math.abs(dy) > MAX_Y) {
                        mTouchState = TOUCH_STATE_Y;
                    } else if (dx > MAX_X) {
                        mTouchState = TOUCH_STATE_X;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //判断状态
                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        mTouchView.onSwipe(motionEvent);
                        //如过最后状态是打开 那么就重新初始化
                        if (!mTouchView.isOpen()) {
                            mTouchView = null;
                        }
                        oldHolder = curHolder;
                    }
                    motionEvent.setAction(MotionEvent.ACTION_CANCEL);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
