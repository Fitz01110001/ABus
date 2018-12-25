package com.fitz.abus.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fitz.abus.R;
import com.fitz.abus.base.BaseActivity;
import com.fitz.abus.bean.BusBaseSHBean;
import com.fitz.abus.fitzview.FitzActionBar;
import com.fitz.abus.fitzview.FitzRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

public class BusStationListActivity extends BaseActivity {


    @BindView(R.id.bus_station_list_fitzactionbar)
    FitzActionBar busStationListFitzactionbar;
    @BindView(R.id.bus_station_tv_busname)
    TextView busStationTvBusname;
    @BindView(R.id.row1)
    TableRow row1;
    @BindView(R.id.bus_station_tv_seTime)
    TextView busStationTvSeTime;
    @BindView(R.id.row2)
    TableRow row2;
    @BindView(R.id.bus_station_tv_start_stop)
    TextView busStationTvStartStop;
    @BindView(R.id.bus_station_switch)
    Button busStationSwitch;
    @BindView(R.id.bus_station_tv_end_stop)
    TextView busStationTvEndStop;
    @BindView(R.id.row3)
    TableRow row3;
    @BindView(R.id.bus_station_tableLayout)
    TableLayout busStationTableLayout;
    @BindView(R.id.bus_station_bus_station_list)
    FitzRecyclerView busStationBusStationList;
    private Context mContext;
    private BusBaseSHBean busbaseSH;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (getIntent().getExtras().containsKey("busbaseSH")) {
            busbaseSH = getIntent().getExtras().getParcelable("busbaseSH");
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        initTableView();
    }

    private void initTableView() {
        busStationTvBusname.setText(busbaseSH.getLine_name()+"路");
        busStationTvSeTime.setText(busbaseSH.getStartEarlytime()+" - "+busbaseSH.getStartLatetime());
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
