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

    /**
     * 上海的 busbase 是作为intent参数传入的，设为静态以便切换方向可用
     */
    protected static BusBaseSHBean busbaseSH;
    protected static BusStopSHBean busStopSHBean;
    protected static BusStopWHBean busStopWHBean;
    protected static BusInfo busInfo;
    private static StopListRecycleAdapter stopListRecycleAdapter;
    @BindView(R.id.bus_station_list_fitzactionbar) FitzActionBar busStationListFitzactionbar;
    @BindView(R.id.tv_line_name) TextView busStationTvBusName;
    @BindView(R.id.tv_start_stop) TextView busStationTvStartStop;
    @BindView(R.id.tv_end_stop) TextView busStationTvEndStop;
    @BindView(R.id.bus_station_switch) ImageButton busStationSwitch;
    @BindView(R.id.tv_seTime) TextView busStationSeTime;
    @BindView(R.id.bus_station_stop_list) FitzRecyclerView busStationStopList;
    @BindView(R.id.return_main) FloatingActionButton returnMain;
    @BindView(R.id.busbase_group) ConstraintLayout busbaseGroup;
    private List<Stops> list_sh = new ArrayList<>();
    private List<List<String>> list_wh = new ArrayList<>();
    private Context context;
    private FitzHttpUtils.AbstractHttpCallBack mBusStationCallBack;
    public static final String EXTRAS_SH = "busbaseSH";
    public static final String EXTRAS_WH = "busbaseWH";
    private static QMUITipDialog tipDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // 每次进入应该把方向重置
        FitzApplication.direction = true;
        tipDialog = new QMUITipDialog.Builder(context).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                                      .setTipWord(context.getResources().getString(R.string.TipWord)).create();


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
                busInfo = new BusInfo();
                switch (FitzApplication.getInstance().getDefaultCityKey()) {
                    case FitzApplication.keySH:
                        busStopSHBean = new Gson().fromJson(data, BusStopSHBean.class);
                        setBusInfoSH();
                        handleSuccess(busStopSHBean);
                        break;
                    case FitzApplication.keyWH:
                        busStopWHBean = new Gson().fromJson(data, BusStopWHBean.class);
                        setBusInfoWH();
                        handleSuccess(busStopWHBean);
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
                if (getIntent().getExtras().containsKey(EXTRAS_SH)) {
                    busbaseSH = getIntent().getExtras().getParcelable(EXTRAS_SH);
                    new FitzHttpUtils().getBusStopSH(busbaseSH.getLine_name(), busbaseSH.getLineId(), mBusStationCallBack);
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

    private void handleSuccess(BusStopSHBean busStopSHBean) {
        if (busStopSHBean.hasResults()) {
            list_sh.clear();
            list_sh.addAll(busInfo.getShStationList0());
            stopListRecycleAdapter = new StopListRecycleAdapter(context, busbaseSH, list_sh);
            initRecycleView();
        }
    }

    private void handleSuccess(BusStopWHBean busStopWHBean) {
        list_wh.clear();
        list_wh.addAll(busInfo.getWhStationList0());
        FLOG("list_wh:"+list_wh);
        //stopListRecycleAdapter = new StopListRecycleAdapter(context, busbaseSH, list_sh);
        //initRecycleView();
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
        busStationTvBusName.setText(busInfo.getBusName());
        busStationSeTime.setText(direction ? busInfo.getSeTimeDirection0() : busInfo.getSeTimeDirection1());
        busStationTvStartStop.setText(direction ? busInfo.getStartStopDirection0() : busInfo.getStartStopDirection1());
        busStationTvEndStop.setText(direction ? busInfo.getEndStopDirection0() : busInfo.getEndStopDirection1());
    }

    private void setBusInfoSH() {
        busInfo.setShStationList0(busStopSHBean.getLineResults0().getStops());
        busInfo.setShStationList1(busStopSHBean.getLineResults1().getStops());
        busInfo.setBusName(busbaseSH.getLine_name() + getResources().getString(R.string.line));
        busInfo.setSeTimeDirection0(busbaseSH.getStartEarlytime() + " - " + busbaseSH.getStartLatetime());
        busInfo.setStartStopDirection0(busbaseSH.getStartStop());
        busInfo.setEndStopDirection0(busbaseSH.getEnd_stop());

        busInfo.setSeTimeDirection1(busbaseSH.getEndEarlytime() + " - " + busbaseSH.getEndLatetime());
        busInfo.setStartStopDirection1(busbaseSH.getEnd_stop());
        busInfo.setEndStopDirection1(busbaseSH.getStartStop());
    }

    private void setBusInfoWH() {
        busInfo.setWhStationList0(busStopWHBean.getResult().getUpLineStationList());
        busInfo.setWhStationList1(busStopWHBean.getResult().getDownLineStationList());
        busInfo.setBusName(busStopWHBean.getResult().getLineName());
        FLOG("businfoWH:" + busStopWHBean.getResult().toString());
        busInfo.setSeTimeDirection0(busStopWHBean.getResult().getStartendTime().split(",")[0]);
        busInfo.setStartStopDirection0(busStopWHBean.getResult().getDownLine().split("开往")[1]);
        busInfo.setEndStopDirection0(busStopWHBean.getResult().getUpLine().split("开往")[1]);

        busInfo.setSeTimeDirection1(busStopWHBean.getResult().getStartendTime().split(",")[1]);
        busInfo.setStartStopDirection1(busStopWHBean.getResult().getUpLine().split("开往")[0]);
        busInfo.setEndStopDirection1(busStopWHBean.getResult().getDownLine().split("开往")[0]);
    }

    @OnClick(R.id.bus_station_switch)
    public void onViewClicked() {
        FLOG("onViewClicked");
        FitzApplication.direction = !FitzApplication.direction;
        updateBusInfo(FitzApplication.direction);
        list_sh.clear();
        list_sh.addAll(FitzApplication.direction ? busInfo.getShStationList0() : busInfo.getShStationList1());
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

    public class BusInfo {
        private String busName;
        private String seTimeDirection0;
        private String startStopDirection0;
        private String endStopDirection0;
        private String seTimeDirection1;
        private String startStopDirection1;
        private String endStopDirection1;
        private List<Stops> shStationList0;
        private List<Stops> shStationList1;
        private List<List<String>> whStationList0;
        private List<List<String>> whStationList1;

        public String getBusName() {
            return busName;
        }

        public void setBusName(String busName) {
            this.busName = busName;
        }

        public String getSeTimeDirection0() {
            return seTimeDirection0;
        }

        public void setSeTimeDirection0(String seTimeDirection0) {
            this.seTimeDirection0 = seTimeDirection0;
        }

        public String getStartStopDirection0() {
            return startStopDirection0;
        }

        public void setStartStopDirection0(String startStopDirection0) {
            this.startStopDirection0 = startStopDirection0;
        }

        public String getEndStopDirection0() {
            return endStopDirection0;
        }

        public void setEndStopDirection0(String endStopDirection0) {
            this.endStopDirection0 = endStopDirection0;
        }

        public String getSeTimeDirection1() {
            return seTimeDirection1;
        }

        public void setSeTimeDirection1(String seTimeDirection1) {
            this.seTimeDirection1 = seTimeDirection1;
        }

        public String getStartStopDirection1() {
            return startStopDirection1;
        }

        public void setStartStopDirection1(String startStopDirection1) {
            this.startStopDirection1 = startStopDirection1;
        }

        public String getEndStopDirection1() {
            return endStopDirection1;
        }

        public void setEndStopDirection1(String endStopDirection1) {
            this.endStopDirection1 = endStopDirection1;
        }

        public List<Stops> getShStationList0() {
            return shStationList0;
        }

        public void setShStationList0(List<Stops> shStationList0) {
            this.shStationList0 = shStationList0;
        }

        public List<Stops> getShStationList1() {
            return shStationList1;
        }

        public void setShStationList1(List<Stops> shStationList1) {
            this.shStationList1 = shStationList1;
        }

        public List<List<String>> getWhStationList0() {
            return whStationList0;
        }

        public void setWhStationList0(List<List<String>> whStationList0) {
            this.whStationList0 = whStationList0;
        }

        public List<List<String>> getWhStationList1() {
            return whStationList1;
        }

        public void setWhStationList1(List<List<String>> whStationList1) {
            this.whStationList1 = whStationList1;
        }
    }

}
