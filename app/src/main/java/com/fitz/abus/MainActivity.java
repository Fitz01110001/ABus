package com.fitz.abus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fitz.abus.base.BaseActivity;
import com.fitz.abus.base.BaseFragment;
import com.fitz.abus.fitzView.FitzActionBar;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.action_bar_button_back)
    ImageButton actionBarButtonBack;
    @BindView(R.id.action_bar_tv_city)
    TextView actionBarTvCity;
    @BindView(R.id.action_bar_button_options)
    ImageButton actionBarButtonOptions;
    @BindView(R.id.fitzactionbar)
    FitzActionBar fitzactionbar;
    @BindView(R.id.main_ConstraintLayout)
    ConstraintLayout mainConstraintLayout;
    @BindView(R.id.main_button_add)
    QMUIRoundButton mainButtonAdd;

    private QMUIListPopup mListPopup;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

    }

    @OnClick(R.id.main_button_add)
    public void onViewClicked() {

    }


    protected Context getContext() {
        return mContext;
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected Activity getCurrtentActivity() {
        return this;
    }

    /*主界面可以点击城市列表*/
    @Override
    protected boolean isCitySelectable() {
        return true;
    }

    /*不显示返回按键*/
    @Override
    protected int isBackVisible() {
        return View.GONE;
    }

    /*主界面显示城市选项*/
    @Override
    protected int isCityVisible() {
        return View.VISIBLE;
    }

    /*主界面显示选项按键*/
    @Override
    protected int isOptionVisible() {
        return View.VISIBLE;
    }


}
