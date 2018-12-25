package com.fitz.abus.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.fitz.abus.R;
import com.fitz.abus.adapter.BusLineRecycleAdapter;
import com.fitz.abus.base.BaseActivity;
import com.fitz.abus.bean.BusBaseSHBean;
import com.fitz.abus.fitzview.FitzActionBar;
import com.fitz.abus.fitzview.FitzRecyclerView;
import com.fitz.abus.utils.FitzHttpUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.add_textview_inputLine) EditText addTextViewInputLine;
    @BindView(R.id.add_textview_input_prompt) TextView addTextViewInputPrompt;
    @BindView(R.id.add_recycler_view) FitzRecyclerView addRecyclerView;
    private Context mContext;
    private FitzHttpUtils.AbstractHttpCallBack mBusBaseCallBack;
    private static List<BusBaseSHBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mBusBaseCallBack = new FitzHttpUtils.AbstractHttpCallBack() {
            /**
             * 查询前准备
             */
            @Override
            public void onCallBefore() {
                super.onCallBefore();
                addTextViewInputPrompt.setText("等一哈不要急");
            }

            /**
             * 获取成功
             *
             * @param data 结果
             */
            @Override
            public void onCallSuccess(String data) {
                super.onCallSuccess(data);
                FLOG("onCallSuccess" + data);
                BusBaseSHBean busBaseSHBean = new Gson().fromJson(data, BusBaseSHBean.class);
                FLOG("onCallSuccess" + busBaseSHBean.getLine_name() + busBaseSHBean.getEnd_stop());
                handleSuccess(busBaseSHBean);
            }

            /**
             * 获取失败
             *
             * @param meg 错误
             */
            @Override
            public void onCallError(String meg) {
                super.onCallError(meg);
                addTextViewInputPrompt.setVisibility(View.VISIBLE);
                addRecyclerView.setVisibility(View.GONE);
                addTextViewInputPrompt.setText("shit happens !!!\n");
            }
        };
    }

    private void handleSuccess(BusBaseSHBean busBaseSHBean) {
        if (busBaseSHBean.nonNull()) {
            list.clear();
            list.add(busBaseSHBean);
            list.add(busBaseSHBean);
            list.add(busBaseSHBean);
            list.add(busBaseSHBean);
            list.add(busBaseSHBean);
        }
        if(addRecyclerView.getVisibility() != View.VISIBLE){
            addTextViewInputPrompt.setVisibility(View.GONE);
            addRecyclerView.setVisibility(View.VISIBLE);
            initRecycleView();
        }
        //设置Adapter
        addRecyclerView.setAdapter(new BusLineRecycleAdapter(mContext, list));

    }

    private void initRecycleView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        //设置布局管理器
        addRecyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置分隔线
        addRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, OrientationHelper.VERTICAL));
        //设置增加或删除条目的动画
        addRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 键盘搜索点击响应
        addTextViewInputLine.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //do something
                    new FitzHttpUtils().getBusBaseSH(addTextViewInputLine.getText().toString(), mBusBaseCallBack);
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
