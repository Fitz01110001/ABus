package com.fitz.abus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fitz.abus.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    public void to2(View view) {
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }
}
