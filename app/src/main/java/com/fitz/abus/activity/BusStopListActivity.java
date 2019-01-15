package com.fitz.abus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;
import com.fitz.abus.adapter.StopListRecycleAdapter;
import com.fitz.abus.base.BaseActivity;
import com.fitz.abus.bean.ArriveBusInfo;
import com.fitz.abus.bean.BusBaseInfoDB;
import com.fitz.abus.bean.BusBaseSHBean;
import com.fitz.abus.bean.BusStopSHBean;
import com.fitz.abus.bean.BusStopWHBean;
import com.fitz.abus.bean.Stops;
import com.fitz.abus.fitzview.FitzActionBar;
import com.fitz.abus.fitzview.FitzRecyclerView;
import com.fitz.abus.utils.FitzHttpUtils;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.fitzView
 * @ClassName: BusStopListActivity
 * @Author: Fitz
 * @CreateDate: 2018/12/20 13:10
 */
public class BusStopListActivity extends BaseActivity {

    public static final String EXTRAS_BBS_SH = "busBaseSHBeanDB";
    public static final String EXTRAS_BBI_SH = "busBaseInfoDB";
    public static final String EXTRAS_WH = "busbaseWH";
    /**
     * 上海的 busbase 是作为intent参数传入的，设为静态以便切换方向可用
     */
    protected static BusBaseInfoDB busBaseInfoDB;
    protected static BusStopWHBean busStopWHBean;
    private static StopListRecycleAdapter stopListRecycleAdapter;
    private static QMUITipDialog tipDialog;
    @BindView(R.id.bus_station_list_fitzactionbar) FitzActionBar busStationListFitzactionbar;
    @BindView(R.id.tv_line_name) TextView busStationTvBusName;
    @BindView(R.id.tv_start_stop) TextView busStationTvStartStop;
    @BindView(R.id.tv_end_stop) TextView busStationTvEndStop;
    @BindView(R.id.bus_station_switch) ImageButton busStationSwitch;
    @BindView(R.id.tv_seTime) TextView busStationSeTime;
    @BindView(R.id.bus_station_stop_list) FitzRecyclerView busStationStopList;
    @BindView(R.id.return_main) FloatingActionButton returnMain;
    @BindView(R.id.busbase_group) ConstraintLayout busbaseGroup;
    private static List<ArriveBusInfo> arriveBusInfoList = new ArrayList<>();
    private Context context;
    private FitzHttpUtils.AbstractHttpCallBack mBusStationCallBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        busBaseInfoDB = new BusBaseInfoDB();
        // 每次进入应该把方向重置
        FitzApplication.direction = true;
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(context.getResources().getString(R.string.TipWord))
                .create();

        mBusStationCallBack = new FitzHttpUtils.AbstractHttpCallBack() {
            @Override
            public void onCallBefore() {
                super.onCallBefore();
                tipDialog.show();
            }

            @Override
            public void onCallSuccess(String data) {
                super.onCallSuccess(data);
                tipDialog.dismiss();
                switch (FitzApplication.getInstance().getDefaultCityKey()) {
                    case FitzApplication.keySH:
                        BusStopSHBean busStopSHBean = new Gson().fromJson(data, BusStopSHBean.class);
                        setBusInfoSH(null, busStopSHBean);
                        handleSuccess();
                        break;
                    case FitzApplication.keyWH:
                        busStopWHBean = new Gson().fromJson(data, BusStopWHBean.class);
                        setBusInfoWH();
                        handleSuccess();
                        break;
                    case FitzApplication.keyNJ:
                        break;
                    default:
                }
                updateBusInfo(FitzApplication.direction);
            }

            @Override
            public void onCallError(String meg) {
                super.onCallError(meg);
                tipDialog.dismiss();
            }
        };

        switch (FitzApplication.getInstance().getDefaultCityKey()) {
            case FitzApplication.keySH:
                FitzApplication.direction = true;
                if (getIntent().getExtras().containsKey(EXTRAS_BBS_SH)) {
                    BusBaseSHBean busBaseSHBean = getIntent().getExtras().getParcelable(EXTRAS_BBS_SH);
                    setBusInfoSH(busBaseSHBean, null);
                    new FitzHttpUtils().getBusStopSH(busBaseSHBean.getLine_name(), busBaseSHBean.getLineId(), mBusStationCallBack);
                } else if (getIntent().getExtras().containsKey(EXTRAS_BBI_SH)) {
                    busBaseInfoDB = getIntent().getExtras().getParcelable(EXTRAS_BBI_SH);
                    FLOG("busBaseInfoDB:" + busBaseInfoDB.toString());
                    new FitzHttpUtils().getBusStopSH(busBaseInfoDB.getBusName(), busBaseInfoDB.getLineId(), mBusStationCallBack);
                }
                break;
            case FitzApplication.keyWH:
                String lineName = getIntent().getExtras().getString(EXTRAS_WH);
                if (lineName != null) {
                    new FitzHttpUtils().postBusLineDetails(lineName, mBusStationCallBack);
                }
                break;
            case FitzApplication.keyNJ:
                break;
            default:
        }
    }

    private void handleSuccess() {
        arriveBusInfoList.clear();
        arriveBusInfoList.addAll(convertJsonList2ArriveBusInfo(busBaseInfoDB));
        stopListRecycleAdapter = new StopListRecycleAdapter(context, busBaseInfoDB, arriveBusInfoList);
        initRecycleView();
    }

    private List<ArriveBusInfo> convertJsonList2ArriveBusInfo(BusBaseInfoDB busBaseInfoDB) {
        List<ArriveBusInfo> list = new ArrayList<>();
        switch (FitzApplication.getInstance().getDefaultCityKey()) {
            case FitzApplication.keySH:
                for (Stops s : FitzApplication.direction ? busBaseInfoDB.getShStationList0() : busBaseInfoDB.getShStationList1()) {
                    list.add(new ArriveBusInfo(s.getZdmc(), s.getId()));
                }
                break;
            case FitzApplication.keyWH:
                for (List<String> l : FitzApplication.direction ? busBaseInfoDB.getWhStationList0() : busBaseInfoDB.getWhStationList1()) {
                    list.add(new ArriveBusInfo(l.get(1), l.get(0)));
                }
                break;
            case FitzApplication.keyNJ:
                break;
            default:
        }
        return list;
    }

    private void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        //设置布局管理器
        busStationStopList.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        busStationStopList.setAdapter(stopListRecycleAdapter);
        //设置分隔线
        busStationStopList.addItemDecoration(new DividerItemDecoration(context, OrientationHelper.VERTICAL));
        //设置增加或删除条目的动画
        busStationStopList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        returnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 显示bus基本信息
     * 根据方向显示选择显示内容
     */
    private void updateBusInfo(boolean direction) {
        busbaseGroup.setVisibility(View.VISIBLE);
        String s = busBaseInfoDB
                .getBusName()
                .contains(getResources().getString(R.string.line)) ? busBaseInfoDB.getBusName() : busBaseInfoDB.getBusName() + getResources()
                .getString(R.string.line);
        busStationTvBusName.setText(s);
        busStationSeTime.setText(direction ? busBaseInfoDB.getStartEndTimeDirection0() : busBaseInfoDB.getStartEndTimeDirection1());
        busStationTvStartStop.setText(direction ? busBaseInfoDB.getStartStopDirection0() : busBaseInfoDB.getStartStopDirection1());
        busStationTvEndStop.setText(direction ? busBaseInfoDB.getEndStopDirection0() : busBaseInfoDB.getEndStopDirection1());
    }

    private void setBusInfoSH(BusBaseSHBean busBaseSHBean, BusStopSHBean busStopSHBean) {
        if (busBaseSHBean != null) {
            busBaseInfoDB.setBusName(busBaseSHBean.getLine_name());
            busBaseInfoDB.setLineId(busBaseSHBean.getLineId());
            busBaseInfoDB.setStartEndTimeDirection0(busBaseSHBean.getStartEarlytime() + " - " + busBaseSHBean.getStartLatetime());
            busBaseInfoDB.setStartStopDirection0(busBaseSHBean.getStartStop());
            busBaseInfoDB.setEndStopDirection0(busBaseSHBean.getEnd_stop());
            busBaseInfoDB.setStartEndTimeDirection1(busBaseSHBean.getEndEarlytime() + " - " + busBaseSHBean.getEndLatetime());
            busBaseInfoDB.setStartStopDirection1(busBaseSHBean.getEnd_stop());
            busBaseInfoDB.setEndStopDirection1(busBaseSHBean.getStartStop());
        }
        if (busStopSHBean != null) {
            busBaseInfoDB.setShStationList0(busStopSHBean.getLineResults0().getStops());
            busBaseInfoDB.setShStationList1(busStopSHBean.getLineResults1().getStops());
        }
    }

    private void setBusInfoWH() {
        busBaseInfoDB.setWhStationList0(busStopWHBean.getResult().getUpLineStationList());
        busBaseInfoDB.setWhStationList1(busStopWHBean.getResult().getDownLineStationList());
        busBaseInfoDB.setBusName(busStopWHBean.getResult().getLineName());
        FLOG("businfoWH:" + busStopWHBean.getResult().toString());
        busBaseInfoDB.setStartEndTimeDirection0(busStopWHBean.getResult().getStartendTime().split(",")[0]);
        busBaseInfoDB.setStartStopDirection0(busStopWHBean.getResult().getDownLine().split("开往")[1]);
        busBaseInfoDB.setEndStopDirection0(busStopWHBean.getResult().getUpLine().split("开往")[1]);

        busBaseInfoDB.setStartEndTimeDirection1(busStopWHBean.getResult().getStartendTime().split(",")[1]);
        busBaseInfoDB.setStartStopDirection1(busStopWHBean.getResult().getUpLine().split("开往")[1]);
        busBaseInfoDB.setEndStopDirection1(busStopWHBean.getResult().getDownLine().split("开往")[1]);
    }

    @OnClick(R.id.bus_station_switch)
    public void onViewClicked() {
        FLOG("switch direction");
        FitzApplication.direction = !FitzApplication.direction;
        updateBusInfo(FitzApplication.direction);
        arriveBusInfoList.clear();
        arriveBusInfoList.addAll(convertJsonList2ArriveBusInfo(busBaseInfoDB));
        // 去除选中图标，更新list
        stopListRecycleAdapter.setSelectedIndex(-1);
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
        return context;
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


}
