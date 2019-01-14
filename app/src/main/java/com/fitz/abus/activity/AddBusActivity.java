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

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;
import com.fitz.abus.adapter.BusLineRecycleAdapter;
import com.fitz.abus.base.BaseActivity;
import com.fitz.abus.bean.BusBaseInfoDB;
import com.fitz.abus.bean.BusBaseSHBean;
import com.fitz.abus.bean.BusBaseWHBean;
import com.fitz.abus.bean.BusStopSHBean;
import com.fitz.abus.fitzview.FitzActionBar;
import com.fitz.abus.fitzview.FitzRecyclerView;
import com.fitz.abus.utils.FitzHttpUtils;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.BindView;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.fitzView
 * @ClassName: AddBusActivity
 * @Author: Fitz
 * @CreateDate: 2018/12/20 13:10
 */
public class AddBusActivity extends BaseActivity {

    private static FitzHttpUtils.AbstractHttpCallBack mBusBaseCallBack;
    private static QMUITipDialog tipDialog;
    private static BusBaseInfoDB busBaseInfoDB;
    @BindView(R.id.add_fitzactionbar)
    FitzActionBar addFitzactionbar;
    @BindView(R.id.add_textview_inputLine)
    EditText addTextViewInputLine;
    @BindView(R.id.add_textview_input_prompt)
    TextView addTextViewInputPrompt;
    @BindView(R.id.add_recycler_view)
    FitzRecyclerView addRecyclerView;
    private Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = this;
        mBusBaseCallBack = new FitzHttpUtils.AbstractHttpCallBack() {
            /**
             * 查询前准备
             */
            @Override
            public void onCallBefore() {
                super.onCallBefore();
                tipDialog = new QMUITipDialog.Builder(mcontext).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).setTipWord("查询中").create();
                tipDialog.show();
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
                tipDialog.dismiss();
                switch (FitzApplication.getInstance().getDefaultCityKey()) {
                    case FitzApplication.keySH:
                        BusBaseSHBean busBaseSHBean = new Gson().fromJson(data, BusBaseSHBean.class);
                        FLOG("callsuccess busBaseSHBean:" + busBaseSHBean.toString());
                        addRecyclerView.setAdapter(new BusLineRecycleAdapter(mcontext, busBaseSHBean));
                        handleSuccess();
                        break;
                    case FitzApplication.keyWH:
                        BusBaseWHBean busBaseWHBean = new Gson().fromJson(data, BusBaseWHBean.class);
                        addRecyclerView.setAdapter(new BusLineRecycleAdapter(mcontext, busBaseWHBean.getResult().getList()));
                        handleSuccess();
                        break;
                    case FitzApplication.keyNJ:
                        break;

                    default:

                        break;
                }
            }

            /**
             * 获取失败
             *
             * @param meg 错误
             */
            @Override
            public void onCallError(String meg) {
                super.onCallError(meg);
                tipDialog.dismiss();
                // TODO: 2018/12/26 应用在后台是可能出现空指针
                addTextViewInputPrompt.setVisibility(View.VISIBLE);
                addRecyclerView.setVisibility(View.GONE);
                addTextViewInputPrompt.setText("shit happens !!!\n");
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();

        // 键盘搜索点击响应
        addTextViewInputLine.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    switch (FitzApplication.getInstance().getDefaultCityKey()) {
                        case FitzApplication.keySH:
                            new FitzHttpUtils().getBusBaseSH(addTextViewInputLine.getText().toString(), mBusBaseCallBack);
                            return true;
                        case FitzApplication.keyWH:
                            new FitzHttpUtils().postBusBaseWH(addTextViewInputLine.getText().toString(), mBusBaseCallBack);
                            break;

                        case FitzApplication.keyNJ:
                            break;

                        default:

                            break;
                    }

                }
                return false;
            }
        });
    }

    private void shGson2BusBaseInfo(BusStopSHBean busStopSHBean) {

    }

    private void handleSuccess() {
        if (addRecyclerView.getVisibility() != View.VISIBLE) {
            addTextViewInputPrompt.setVisibility(View.GONE);
            addRecyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);
            //设置布局管理器
            addRecyclerView.setLayoutManager(layoutManager);
            //设置为垂直布局，这也是默认的
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            //设置分隔线
            addRecyclerView.addItemDecoration(new DividerItemDecoration(mcontext, OrientationHelper.VERTICAL));
            //设置增加或删除条目的动画
            addRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
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
        return mcontext;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
