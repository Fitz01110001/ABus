package com.fitz.abus.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.PopupMenu;

import com.fitz.abus.R;
import com.fitz.abus.fitzView.FitzActionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {


    private boolean isDebug = true;
    private String TAG = "fitz-" + this.getClass().getSimpleName();
    private Unbinder mUnbinder;
    /**
     * 是否禁止旋转屏幕
     */
    private boolean isAllowScreenRoate = false;
    /**
     * 是否允许全屏
     */
    private boolean mAllowFullScreen = true;
    private FitzActionBar mFitzActionBar;
    private View.OnClickListener mActionBarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_bar_button_back:
                    FLOG("click actionbar back");
                    getCurrtentActivity().finish();
                    break;
                case R.id.action_bar_tv_city:
                    FLOG("click actionbar city");
                    if (isCitySelectable()) {
                        showPopMenu(R.menu.menu_cities, v);
                    }
                    break;
                case R.id.action_bar_button_options:
                    FLOG("click actionbar options");
                    showPopMenu(R.menu.menu_options, v);
                    break;
                default:
                    //nothing
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 一些系统配置
        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        setContentView(getContentViewResId());
        mUnbinder = ButterKnife.bind(this);

        //设置状态栏颜色
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent, null));

        setMyActionBar();

    }

    protected void setMyActionBar() {
        mFitzActionBar = (FitzActionBar) findViewById(R.id.fitzactionbar);
        mFitzActionBar.setData(mActionBarClickListener, isBackVisible(), isCityVisible(), isOptionVisible());
    }


    /**
     * 获取actionBar
     *
     * @return
     */
    protected FitzActionBar getMyActionBar() {
        return mFitzActionBar;
    }

    protected void showPopMenu(int res, View view) {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(this, view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(res, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.options_settings:
                        FLOG("click options_settings");
                        break;
                    case R.id.options_about:
                        FLOG("click options_about");
                        break;
                    case R.id.options_feedback:
                        FLOG("click options_feedback");
                        break;
                    case R.id.cities1:
                        FLOG("click cities1");
                        break;
                    case R.id.cities2:
                        FLOG("click cities2");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // 控件消失时的事件
            }
        });
    }


    protected abstract int getContentViewResId();


    protected abstract Activity getCurrtentActivity();

    // 只在主界面可点击
    protected abstract boolean isCitySelectable();

    // 主界面不应显示
    protected abstract int isBackVisible();

    // 应始终显示
    protected abstract int isCityVisible();

    // 应始终显示
    protected abstract int isOptionVisible();

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
