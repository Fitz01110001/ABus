package com.fitz.abus.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fitz.abus.R;
import com.fitz.abus.adapter.StopListRecycleAdapter;
import com.fitz.abus.base.BaseActivity;
import com.fitz.abus.bean.ArriveBaseBean;
import com.fitz.abus.bean.BusBaseSHBean;
import com.fitz.abus.bean.BusStopSHBean;
import com.fitz.abus.bean.Stops;
import com.fitz.abus.fitzview.FitzActionBar;
import com.fitz.abus.fitzview.FitzRecyclerView;
import com.fitz.abus.utils.FitzHttpUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BusStopListActivity extends BaseActivity {


    private static List<Stops> list = new ArrayList<>();
    @BindView(R.id.bus_station_list_fitzactionbar)
    FitzActionBar busStationListFitzactionbar;
    @BindView(R.id.bus_station_tv_bus_name)
    TextView busStationTvBusName;
    @BindView(R.id.bus_station_tv_start_stop)
    TextView busStationTvStartStop;
    @BindView(R.id.bus_station_tv_end_stop)
    TextView busStationTvEndStop;
    @BindView(R.id.bus_station_switch)
    ImageButton busStationSwitch;
    @BindView(R.id.bus_station_se_time)
    TextView busStationSeTime;
    @BindView(R.id.bus_station_stop_list)
    FitzRecyclerView busStationStopList;
    private Context mContext;
    private BusBaseSHBean busbaseSH;
    private FitzHttpUtils.AbstractHttpCallBack mBusStationCallBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (getIntent().getExtras().containsKey("busbaseSH")) {
            busbaseSH = getIntent().getExtras().getParcelable("busbaseSH");
        }
        initTableView();
        mBusStationCallBack = new FitzHttpUtils.AbstractHttpCallBack() {
            @Override
            public void onCallBefore() {
                super.onCallBefore();
                // TODO: 2018/12/26 加载等待动画
            }

            @Override
            public void onCallSuccess(String data) {
                super.onCallSuccess(data);
                BusStopSHBean busStopSHBean = new Gson().fromJson(data, BusStopSHBean.class);
                handleSuccess(busStopSHBean);
            }

            @Override
            public void onCallError(String meg) {
                super.onCallError(meg);
            }
        };
    }

    private void handleSuccess(BusStopSHBean busStopSHBean) {
        if (busStopSHBean.nonNull()) {
            list.clear();
            list = busStopSHBean.getLineResults0().getStops();
        }
        initRecycleView();
        busStationStopList.setAdapter(new StopListRecycleAdapter(mContext, busbaseSH.getLine_name(), busbaseSH.getLineId(),busStopSHBean.getLineResults1().getDirection(), list));
    }

    private void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        //设置布局管理器
        busStationStopList.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置分隔线
        busStationStopList.addItemDecoration(new DividerItemDecoration(mContext, OrientationHelper.VERTICAL));
        //设置增加或删除条目的动画
        busStationStopList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (busbaseSH != null) {
            new FitzHttpUtils().getBusStopSH(busbaseSH.getLine_name(), busbaseSH.getLineId(), mBusStationCallBack);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initTableView() {
        busStationTvBusName.setText(busbaseSH.getLine_name() + getResources().getString(R.string.line));
        busStationSeTime.setText(busbaseSH.getStartEarlytime() + " - " + busbaseSH.getStartLatetime());
        busStationTvStartStop.setText(busbaseSH.getStartStop());
        busStationTvEndStop.setText(busbaseSH.getEnd_stop());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected Context getContext() {
        return mContext;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_bus_station_list;
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
        return View.GONE;
    }

    @Override
    protected boolean isCitySelectable() {
        return false;
    }

    @Override
    protected int getContentActionBarResId() {
        return R.id.bus_station_list_fitzactionbar;
    }

    @OnClick(R.id.bus_station_switch)
    public void onViewClicked() {}
}