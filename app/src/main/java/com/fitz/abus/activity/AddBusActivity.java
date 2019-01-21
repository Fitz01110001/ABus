package com.fitz.abus.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;
import com.fitz.abus.adapter.BusLineRecycleAdapter;
import com.fitz.abus.base.BaseActivity;
import com.fitz.abus.bean.BusBaseSHBean;
import com.fitz.abus.bean.BusBaseWHBean;
import com.fitz.abus.fitzview.FitzActionBar;
import com.fitz.abus.fitzview.FitzRecyclerView;
import com.fitz.abus.utils.FitzHttpUtils;
import com.fitz.abus.utils.SpacesItemDecoration;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.add_fitzactionbar) FitzActionBar addFitzactionbar;
    @BindView(R.id.add_textview_inputLine) EditText addTextViewInputLine;
    @BindView(R.id.add_textview_input_prompt) TextView addTextViewInputPrompt;
    @BindView(R.id.add_recycler_view) FitzRecyclerView addRecyclerView;
    @BindView(R.id.button_search) QMUIRoundButton buttonSearch;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mBusBaseCallBack = new FitzHttpUtils.AbstractHttpCallBack() {
            @Override
            public void onCallBefore() {
                super.onCallBefore();
                tipDialog = new QMUITipDialog.Builder(context).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).setTipWord("查询中").create();
                tipDialog.show();
                addTextViewInputPrompt.setText("等一哈不要急");
            }

            @Override
            public void onCallSuccess(String data) {
                super.onCallSuccess(data);
                tipDialog.dismiss();
                switch (FitzApplication.getInstance().getDefaultCityKey()) {
                    case FitzApplication.keySH:
                        BusBaseSHBean busBaseSHBean = new Gson().fromJson(data, BusBaseSHBean.class);
                        FLOG("callsuccess busBaseSHBean:" + busBaseSHBean.toString());
                        addRecyclerView.setAdapter(new BusLineRecycleAdapter(context, busBaseSHBean));
                        handleSuccess();
                        break;
                    case FitzApplication.keyWH:
                        BusBaseWHBean busBaseWHBean = new Gson().fromJson(data, BusBaseWHBean.class);
                        addRecyclerView.setAdapter(new BusLineRecycleAdapter(context, busBaseWHBean.getResult().getList()));
                        handleSuccess();
                        break;
                    case FitzApplication.keyNJ:
                        break;

                    default:

                        break;
                }
            }

            @Override
            public void onCallError(String meg) {
                super.onCallError(meg);
                tipDialog.dismiss();
                addTextViewInputPrompt.setVisibility(View.VISIBLE);
                buttonSearch.setVisibility(View.VISIBLE);
                addRecyclerView.setVisibility(View.GONE);
                addTextViewInputPrompt.setText(getContext().getResources().getString(R.string.not_found));
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
                            return true;
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

    private void handleSuccess() {
        if (addRecyclerView.getVisibility() != View.VISIBLE) {
            addTextViewInputPrompt.setVisibility(View.GONE);
            buttonSearch.setVisibility(View.GONE);
            addRecyclerView.setVisibility(View.VISIBLE);
        }
        //LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理器
        addRecyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置分隔线
        addRecyclerView.addItemDecoration(new SpacesItemDecoration(5, 5, 10, 10));
        //设置增加或删除条目的动画
        addRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
    protected int isLocationImageVisible() {
        return View.GONE;
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
        return context;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @OnClick(R.id.button_search)
    public void onViewClicked() {
        switch (FitzApplication.getInstance().getDefaultCityKey()) {
            case FitzApplication.keySH:
                new FitzHttpUtils().getBusBaseSH(addTextViewInputLine.getText().toString(), mBusBaseCallBack);
                break;
            case FitzApplication.keyWH:
                new FitzHttpUtils().postBusBaseWH(addTextViewInputLine.getText().toString(), mBusBaseCallBack);
                break;
            case FitzApplication.keyNJ:
                break;
            default:
                break;
        }
    }
}
