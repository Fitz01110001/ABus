package com.fitz.abus.base;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fitz.abus.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;
    private boolean isDebug = true;
    private String TAG = "fitz-" + this.getClass().getSimpleName();
    /**  是否禁止旋转屏幕 */
    protected boolean isAllowScreenRoate=false;
    /** 是否允许全屏*/
    protected boolean mAllowFullScreen= true;

    protected TextView mTextView_City;
    protected Drawable mDrawable_location;
    private ImageButton mImageButton_back;
    private ImageButton mImageButton_options;
    private View.OnClickListener mActionBarClickListener;

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {}


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 一些系统配置
        if(!isAllowScreenRoate){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if(mAllowFullScreen){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        setContentView(getContentViewResId());
        mUnbinder = ButterKnife.bind(this);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/

        //设置状态栏颜色
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent,null));

        //自定义actionbar
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.layout_title);//设置自定义的布局：layout_title
        }

        initActionBar();

    }

    @SuppressLint("WrongViewCast")
    private void initActionBar() {
        mTextView_City = findViewById(R.id.action_bar_tv_city);
        mDrawable_location = getResources().getDrawable(R.drawable.location,null);
        mDrawable_location.setBounds(0,0,40,40);
        mTextView_City.setCompoundDrawables(null,null,mDrawable_location,null);

        mImageButton_back = findViewById(R.id.action_bar_button_back);
        mImageButton_options = findViewById(R.id.action_bar_button_options);

        mImageButton_back.setOnClickListener(mActionBarClickListener);
        mImageButton_options.setOnClickListener(mActionBarClickListener);

        mActionBarClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.action_bar_button_back:
                        FLOG("click actionbar back");
                        finish();
                        break;
                    case R.id.action_bar_tv_city:
                        FLOG("click actionbar city");

                        break;
                    case R.id.action_bar_button_options:
                        FLOG("click actionbar options");

                        break;
                        default:
                            //nothing
                }
            }
        };
    }

    protected abstract int getContentViewResId();

    protected abstract void init(Bundle savedInstanceState);

    private void setBackButtonVisible(@Visibility int visibility){
        mImageButton_back.setVisibility(visibility);
    }

    private void setOptionButtonVisible(@Visibility int visibility){
        mImageButton_options.setVisibility(visibility);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    public void FLOG(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

}
