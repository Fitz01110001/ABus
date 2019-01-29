package com.fitz.abus.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;
import com.fitz.abus.base.BaseActivity;
import com.fitz.abus.base.BaseFragment;
import com.fitz.abus.fitzview.FitzActionBar;
import com.fitz.abus.fitzview.GuideView;
import com.fitz.abus.utils.FitzBusFragmentUtils;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.fitzView
 * @ClassName: MainActivity
 * @Author: Fitz
 * @CreateDate: 2018/12/15 13:10
 */
public class MainActivity extends BaseActivity {

    private static final String ARG_TAG = "arg_tag";
    @BindView(R.id.action_bar_button_back) ImageButton actionBarButtonBack;
    @BindView(R.id.action_bar_tv_city) TextView actionBarTvCity;
    @BindView(R.id.action_bar_button_options) ImageButton actionBarButtonOptions;
    @BindView(R.id.main_fitzactionbar) FitzActionBar fitzactionbar;
    @BindView(R.id.main_ConstraintLayout) ConstraintLayout mainConstraintLayout;
    private Context context;
    private AppCompatActivity appCompatActivity;
    private FitzBusFragmentUtils fitzBusFragmentUtils;
    private Boolean isFirstBoot = false;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String CHECK_FIRST_BOOT_KEY = "check_first_boot_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        appCompatActivity = this;
        fitzBusFragmentUtils = new FitzBusFragmentUtils(appCompatActivity);
        BaseFragment fg = new BaseFragment();
        Bundle args = new Bundle();
        String tag = FitzApplication.getInstance().getDefaultCityCode();
        args.putString(ARG_TAG, tag);
        fg.setArguments(args);
        fitzBusFragmentUtils.replaceFragment(fg, tag);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirstBoot = checkFirstBoot();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirstBoot) {
            new GuideView.Builder(context)
                    .addGuideView(new GuideView(this)
                            .setGuideParent(this.findViewById(getContentActionBarResId()).findViewById(R.id.action_bar_tv_city))
                            .setGuideStr(getResources().getString(R.string.guide_string_city)))
                    .addGuideView(new GuideView(this)
                            .setGuideParent(this.findViewById(getContentActionBarResId()).findViewById(R.id.action_bar_button_options))
                            .setGuideStr(getResources().getString(R.string.guide_string_options)))
                    .show();
        }
    }

    private boolean checkFirstBoot() {
        preferences = getSharedPreferences(CHECK_FIRST_BOOT_KEY, MODE_PRIVATE);
        if (preferences.getBoolean(CHECK_FIRST_BOOT_KEY, true)) {
            editor = preferences.edit();
            editor.putBoolean(CHECK_FIRST_BOOT_KEY, false);
            if (!editor.commit()) {
                Log.e("main", "checkFirstBoot error");
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    public int getContentViewResId() {
        return R.layout.main_activity_layout;
    }

    /**
     * 主界面可以点击城市列表
     */
    @Override
    protected boolean isCitySelectable() {
        return true;
    }

    @Override
    protected int getContentActionBarResId() {
        return R.id.main_fitzactionbar;
    }

    @Override
    protected int isLocationImageVisible() {
        return View.VISIBLE;
    }


    /**
     * 不显示返回按键
     */
    @Override
    protected int isBackVisible() {
        return View.GONE;
    }

    /**
     * 主界面显示城市选项
     */
    @Override
    protected int isCityVisible() {
        return View.VISIBLE;
    }

    /**
     * 主界面显示选项按键
     */
    @Override
    protected int isOptionVisible() {
        return View.VISIBLE;
    }

}
