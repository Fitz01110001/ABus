package com.fitz.abus.fitzView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.fitz.abus.R;

public class fitzTextView2 extends android.support.v7.widget.AppCompatTextView {

    private       String TAG = "fitzTextView2";
    private final String ATTR_ICON_SRC = "iconSrc";
    private final String NAMESPACE = "http://fitz.com/view";
    private       Bitmap bitMap;

    public fitzTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        int resouceId = attrs.getAttributeResourceValue(NAMESPACE, ATTR_ICON_SRC, R.drawable.location);
        bitMap = BitmapFactory.decodeResource(getResources(), resouceId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG,"onMeasure");
        Log.d(TAG,"widthMeasureSpec:"+widthMeasureSpec);
        Log.d(TAG,"heightMeasureSpec:"+heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG,"onDraw");
    }


    @Override
    public void setCompoundDrawables(
             @Nullable Drawable left,
             @Nullable Drawable top,
             @Nullable Drawable right,
             @Nullable Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        Log.d(TAG,"setCompoundDrawables");
        Log.d(TAG,"setCompoundDrawables,left+"+left);
        Log.d(TAG,"setCompoundDrawables,top+"+top);
        Log.d(TAG,"setCompoundDrawables,right+"+right);
        Log.d(TAG,"setCompoundDrawables,bottom+"+bottom);




    }
}
