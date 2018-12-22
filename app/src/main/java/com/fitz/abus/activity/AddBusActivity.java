package com.fitz.abus.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.fitz.abus.R;
import com.fitz.abus.base.BaseActivity;
import com.fitz.abus.fitzview.FitzActionBar;

import butterknife.BindView;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.fitzView
 * @ClassName: AddBusActivity
 * @Author: Fitz
 * @CreateDate: 2018/12/20 13:10
 */
public class AddBusActivity extends BaseActivity {

    @BindView(R.id.add_fitzactionbar) FitzActionBar addFitzactionbar;
    @BindView(R.id.add_textview_inputLine) EditText addTextviewInputLine;
    @BindView(R.id.add_textview_input_prompt) TextView addTextviewInputPrompt;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;


        // 键盘搜索点击响应
        addTextviewInputLine.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //do something
                    //doSearch();
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_add_bus;
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
        return View.INVISIBLE;
    }

    @Override
    protected Context getContext() {
        return mContext;
    }


}
