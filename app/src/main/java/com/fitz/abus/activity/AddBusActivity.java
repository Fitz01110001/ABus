package com.fitz.abus.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.fitz.abus.R;
import com.fitz.abus.base.BaseActivity;

public class AddBusActivity extends BaseActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_add_bus;
    }

    @Override
    protected Activity getCurrtentActivity() {
        return null;
    }

    @Override
    protected boolean isCitySelectable() {
        return false;
    }

    @Override
    protected int getContentActionBarResId() {
        return R.id.add_fitzactionbar;
    }

    @Override
    protected int isBackVisible() {
        return View.VISIBLE;
    }

    @Override
    protected int isCityVisible() {
        return View.VISIBLE;
    }

    @Override
    protected int isOptionVisible() {
        return View.VISIBLE;
    }

    @Override
    protected Context getContext() {
        return mContext;
    }
}
