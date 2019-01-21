package com.fitz.abus.fitzview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;

public class FitzActionBar extends RelativeLayout {

    protected TextView mTextView_City;
    protected Context context;
    private boolean isDebug = true;
    private String TAG = "FitzActionBar";
    private View contentView;
    private ImageView imageView_location;
    private ImageButton mImageButton_back;
    private ImageButton mImageButton_options;
    private View.OnClickListener mActionBarClickListener;

    public FitzActionBar(Context context) {
        super(context, null);

    }

    public FitzActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        FLOG("FitzActionBar");
        this.context = context;
        initActionBar();
    }

    public void setData(View.OnClickListener viewClickListener, int backVisible, int cityVisible, int optionsVisible, int locationImageVisible) {
        FLOG("setData");
        mActionBarClickListener = viewClickListener;
        mImageButton_back.setOnClickListener(mActionBarClickListener);
        mTextView_City.setOnClickListener(mActionBarClickListener);
        mImageButton_options.setOnClickListener(mActionBarClickListener);
        mImageButton_back.setVisibility(backVisible);
        mTextView_City.setVisibility(cityVisible);
        mImageButton_options.setVisibility(optionsVisible);
        mTextView_City.setText(FitzApplication.getInstance().getDefaultCityName());
        imageView_location.setVisibility(locationImageVisible);
    }

    private void initActionBar() {
        FLOG("initActionBar");
        contentView = inflate(getContext(), R.layout.layout_title, this);
        mTextView_City = contentView.findViewById(R.id.action_bar_tv_city);
        mImageButton_back = contentView.findViewById(R.id.action_bar_button_back);
        mImageButton_options = contentView.findViewById(R.id.action_bar_button_options);
        imageView_location = contentView.findViewById(R.id.imageView_location);
    }

    public void setDefaultCityTV(String cityName) {
        mTextView_City.setText(cityName);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        FLOG("mTextView_City.getMeasuredHeight()" + mTextView_City.getMeasuredHeight());
    }

    public void FLOG(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }


}
