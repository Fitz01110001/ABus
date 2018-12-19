package com.fitz.abus.base;

import android.app.Activity;
import android.content.Context;
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

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;
import com.fitz.abus.fitzView.FitzActionBar;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {


    private boolean isDebug = true;
    private String TAG = "fitz-" + this.getClass().getSimpleName();
    private Unbinder mUnbinder;
    private QMUIListPopup mListPopup;
    /**
     * 是否禁止旋转屏幕
     */
    private boolean isAllowScreenRoate = false;
    /**
     * 是否允许全屏
     */
    private boolean mAllowFullScreen = true;
    private FitzActionBar mFitzActionBar;
    private FragmentTransaction transaction;
    private static final String ARG_INDEX = "arg_index";


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

    private void showQMUIpopupMenu(View v) {
        if (mListPopup == null) {
            Collection<Integer> collectionCityName = FitzApplication.getInstance().Cities.values();
            final List<Integer> cityNameResID = new ArrayList<>(collectionCityName);
            List<String> cityName = new ArrayList<>();
            for(Iterator<Integer> it= cityNameResID.iterator();it.hasNext();)
            {
                cityName.add(getResources().getString(it.next()));
            }
            Set<String> collectionCityID = FitzApplication.getInstance().Cities.keySet();
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
                    ((BaseFragment) fg).setTV(getResources().getString(cityNameResID.get(i)));
                    FitzApplication.setDefaultCity(Integer.valueOf(cityID.get(i)));
                    mListPopup.dismiss();
                }
            });
            /*mListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                }
            });*/
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

    public static BaseFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        BaseFragment fragment = new BaseFragment();
        fragment.setArguments(args);
        return fragment;
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

    protected abstract Context getContext();

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
