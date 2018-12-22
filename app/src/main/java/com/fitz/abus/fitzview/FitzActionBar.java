package com.fitz.abus.fitzview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;

public class FitzActionBar extends RelativeLayout {

    private boolean isDebug = true;
    private String TAG = "FitzActionBar";
    private View contentView;
    protected TextView mTextView_City;
    protected Drawable mDrawable_location;
    private ImageButton mImageButton_back;
    private ImageButton mImageButton_options;
    private View.OnClickListener mActionBarClickListener;
    private Context mContext;

    public FitzActionBar(Context context) {
        super(context, null);

    }

    public FitzActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        FLOG("FitzActionBar");
        mContext = context;
        initActionBar();
    }



    public void setData(View.OnClickListener viewClickListener, int backVisible, int cityVisible, int optionsVisible) {
        FLOG("setData");
        mActionBarClickListener = viewClickListener;
        mImageButton_back.setOnClickListener(mActionBarClickListener);
        mTextView_City.setOnClickListener(mActionBarClickListener);
        mImageButton_options.setOnClickListener(mActionBarClickListener);
        mImageButton_back.setVisibility(backVisible);
        mTextView_City.setVisibility(cityVisible);
        mImageButton_options.setVisibility(optionsVisible);
        mTextView_City.setText(FitzApplication.getInstance().getDefaultCityName());
    }


    private void initActionBar() {
        FLOG("initActionBar");
        contentView = inflate(getContext(), R.layout.layout_title, this);
        mTextView_City = contentView.findViewById(R.id.action_bar_tv_city);
        mDrawable_location = getResources().getDrawable(R.drawable.location, null);
        mDrawable_location.setBounds(0, 0, 40, 40);
        mTextView_City.setCompoundDrawables(null, null, mDrawable_location, null);
        mImageButton_back = contentView.findViewById(R.id.action_bar_button_back);
        mImageButton_options = contentView.findViewById(R.id.action_bar_button_options);

    }

    public void setDefaultCityTV(String cityName){
        mTextView_City.setText(cityName);
    }

    public void FLOG(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }


}
