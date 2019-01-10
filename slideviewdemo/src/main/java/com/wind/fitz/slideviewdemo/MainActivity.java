package com.wind.fitz.slideviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.wind.fitz.slideviewdemo.adapter.FragmentListAdapter;
import com.wind.fitz.slideviewdemo.fitzview.FitzOnItemTouchListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int max = 20;
    private static final List<Integer> currentBusList = new ArrayList<>();
    private static FragmentListAdapter fragmentListAdapter;
    private RecyclerView fg_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < max; i++) {
            currentBusList.add(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        fg_container = findViewById(R.id.fg_container);

        fragmentListAdapter = new FragmentListAdapter(this, currentBusList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        fg_container.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        fg_container.setAdapter(fragmentListAdapter);
        //设置分隔线
        fg_container.addItemDecoration(new DividerItemDecoration(this, OrientationHelper.VERTICAL));
        //设置增加或删除条目的动画
        fg_container.setItemAnimator(new DefaultItemAnimator());
        fg_container.addOnItemTouchListener(new FitzOnItemTouchListener(this));

    }
}
