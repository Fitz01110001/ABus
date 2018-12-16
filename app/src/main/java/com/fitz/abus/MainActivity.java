package com.fitz.abus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.fitz.abus.base.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity  {

    @BindView(R.id.action_bar_button_back)    ImageButton actionBarButtonBack;
    @BindView(R.id.action_bar_tv_city)        TextView    actionBarTvCity;
    @BindView(R.id.action_bar_button_options) ImageButton actionBarButtonOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopMenu(view);
            }
        });
    }

    protected void showPopMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_cities, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.options_settings:
                        FLOG("click options_settings");
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




    public void to2(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }


}
