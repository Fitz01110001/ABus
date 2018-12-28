package com.fitz.abus.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;
import com.fitz.abus.activity.AddBusActivity;
import com.fitz.abus.fitzview.FitzActionBar;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.base
 * @ClassName: BaseActivity
 * @Author: Fitz
 * @CreateDate: 2018/12/20 13:10
 */

public abstract class BaseActivity extends AppCompatActivity {


    private boolean isDebug = true;
    /**
     * 是否禁止旋转屏幕
     */
    private boolean isAllowScreenRoate = false;
    /**
     * 是否允许全屏
     */
    private boolean mAllowFullScreen = true;
    private String TAG = "fitz-" + this.getClass().getSimpleName();
    private Unbinder mUnbinder;
    private QMUIListPopup mListPopup;
    private FitzActionBar mFitzActionBar;
    private FragmentTransaction transaction;
    private static final String ARG_INDEX = "arg_index";

    /** actionbar 点击事件控制*/
    private View.OnClickListener mActionBarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_bar_button_back:
                    FLOG("click actionbar back");
                    onBackPressed();
                    break;
                case R.id.action_bar_tv_city:
                    FLOG("click actionbar city");
                    if (isCitySelectable()) {
                        showQMUIpopupMenu(v);
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
        //EventBus.getDefault().register(this);
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

        setMyActionBar(getContentActionBarResId());

    }

    protected void setMyActionBar(int res) {
        mFitzActionBar = (FitzActionBar) findViewById(res);
        mFitzActionBar.setData(mActionBarClickListener, isBackVisible(), isCityVisible(), isOptionVisible());
    }

    /** 城市TV 弹出 QMUI popmenu*/
    private void showQMUIpopupMenu(View v) {
        if (mListPopup == null) {
            Collection<Integer> collectionCityName = FitzApplication.Cities.values();
            final List<Integer> cityNameResID = new ArrayList<>(collectionCityName);
            List<String> cityName = new ArrayList<>();
            for(Iterator<Integer> it= cityNameResID.iterator();it.hasNext();)
            {
                cityName.add(getResources().getString(it.next()));
            }

            Set<String> collectionCityID = FitzApplication.Cities.keySet();
            final List<String> cityID = new ArrayList<>(collectionCityID);
            ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.simple_list_item, cityName);

            mListPopup = new QMUIListPopup(getContext(), QMUIPopup.DIRECTION_NONE, adapter);
            mListPopup.create(QMUIDisplayHelper.dp2px(getContext(), 150), QMUIDisplayHelper.dp2px(getContext(), 100), new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Fragment fg = newInstance(i);
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment_container, fg)
                            .addToBackStack("Item " + (i + 1)).show(fg).commit();

                    mListPopup.dismiss();
                    FitzApplication.getInstance().setDefaultCityKey(cityID.get(i));
                    mFitzActionBar.setDefaultCityTV(FitzApplication.getInstance().getDefaultCityName());
                }
            });
            mListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    onDismiss();
                }
            });
        }
        mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mListPopup.setPreferredDirection(QMUIPopup.DIRECTION_TOP);
        mListPopup.show(v);
    }

    /**
     * 获取actionBar
     *
     * @return
     */
    protected FitzActionBar getMyActionBar() {
        return mFitzActionBar;
    }

    /** 选项button 弹出popmenu*/
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
                    case R.id.options_add:
                        // to-do bug
                        Intent intent = new Intent(getContext(),AddBusActivity.class);
                        //intent.addFlags();
                        startActivity(intent);
                        break;
                    case R.id.options_settings:
                        FLOG("click options_settings");
                        break;
                    case R.id.options_about:
                        FLOG("click options_about");
                        break;
                    case R.id.options_feedback:
                        FLOG("click options_feedback");
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

    public static BaseFragment newInstance(int index) {
        Bundle args = new Bundle();
        //args.putInt(ARG_INDEX, index);
        BaseFragment fragment = new BaseFragment();
        //fragment.setArguments(args);
        return fragment;
    }

    /** 获取子类 context*/
    protected abstract Context getContext();

    /** 获取子类view资源*/
    protected abstract int getContentViewResId();

    /** 主界面不应显示*/
    protected abstract int isBackVisible();

    /** 应始终显示*/
    protected abstract int isCityVisible();

    /** 应始终显示*/
    protected abstract int isOptionVisible();

    /** 只在主界面可点击*/
    protected abstract boolean isCitySelectable();

    /** 获取当前activity包含的 actionbar res*/
    protected abstract int getContentActionBarResId();

    @Override
    protected void onStart() {
        super.onStart();
        FLOG("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        FLOG("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        FLOG("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FLOG("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FLOG("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        FLOG("onDestroy");
    }

    public void FLOG(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

}
