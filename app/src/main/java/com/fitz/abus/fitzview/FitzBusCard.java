package com.fitz.abus.fitzview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fitz.abus.R;
import com.fitz.abus.bean.ArriveBaseBean;
import com.fitz.abus.bean.BusLineDB;
import com.fitz.abus.utils.FitzHttpUtils;
import com.google.gson.Gson;

import butterknife.OnClick;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.fitzView
 * @ClassName: FitzBusCard
 * @Author: Fitz
 * @CreateDate: 2018/12/16 20:10
 */
public class FitzBusCard extends TableLayout {


    protected ArriveBaseBean arriveBaseBean;
    private TextView tvLineName;
    private TextView tvStationName;
    private TextView tvStartStop;
    private TextView tvEndStop;
    private TextView tvSeTime;
    private TextView tvStopdis;
    private TextView tvRemainTime;
    private TextView tvDistance;
    private TextView tvPlate;
    private boolean isDebug = true;
    private String TAG = "FitzBusCard";
    private Context context;
    private View contentView;
    private BusLineDB busLineDB;
    private FitzHttpUtils.AbstractHttpCallBack mArriveBaseCallBack;

    public FitzBusCard(Context context, BusLineDB busLineDB) {
        super(context);
        this.context = context;
        this.busLineDB = busLineDB;

        initBusCard();
    }

    private void initBusCard() {
        contentView = inflate(context, R.layout.bus_card, this);

        tvLineName = contentView.findViewById(R.id.tv_line_name);
        tvStationName = contentView.findViewById(R.id.tv_stationName);
        tvStartStop = contentView.findViewById(R.id.tv_start_stop);
        tvEndStop = contentView.findViewById(R.id.tv_end_stop);
        tvSeTime = contentView.findViewById(R.id.tv_seTime);
        tvStopdis = contentView.findViewById(R.id.tv_stopdis);
        tvRemainTime = contentView.findViewById(R.id.tv_remainTime);
        tvDistance = contentView.findViewById(R.id.tv_distance);
        tvPlate = contentView.findViewById(R.id.tv_plate);


        if (busLineDB != null) {
            setTvLineName(busLineDB.getLineName());
            setStationName(busLineDB.getStationName());
            setSETime(busLineDB.getStartTime() + " - " + busLineDB.getEndTime());
            setStartStop(busLineDB.getStartStop());
            setEndStop(busLineDB.getEndStop());
            mArriveBaseCallBack = new FitzHttpUtils.AbstractHttpCallBack() {
                @Override
                public void onCallBefore() {
                    super.onCallBefore();
                }

                @Override
                public void onCallSuccess(String data) {
                    super.onCallSuccess(data);
                    Log.d(TAG, data);
                    arriveBaseBean = new Gson().fromJson(data, ArriveBaseBean.class);
                    setPlate(arriveBaseBean.getCars().get(0).getTerminal());
                    setDistance(arriveBaseBean.getCars().get(0).getDistance());
                    setRemainTime(arriveBaseBean.getCars().get(0).getTime());
                    setStopdis(arriveBaseBean.getCars().get(0).getStopdis());
                }

                @Override
                public void onCallError(String meg) {
                    super.onCallError(meg);
                    Toast.makeText(FitzBusCard.this.context, "等待发车", Toast.LENGTH_SHORT).show();
                }
            };
            new FitzHttpUtils().getArriveBaseSH(busLineDB.getLineName(), busLineDB.getLineID(), busLineDB.getStationID(), busLineDB.getDirection(),
                    mArriveBaseCallBack);

        }
    }

    public void setTvLineName(String LineName) {
        tvLineName.setText(LineName);
    }

    public void setStationName(String StationName) {
        tvStationName.setText(StationName);
    }

    public void setStartStop(String StartStop) {
        tvStartStop.setText(StartStop);
    }

    public void setEndStop(String EndStop) {
        tvEndStop.setText(EndStop);
    }

    public void setSETime(String SeTime) {
        tvSeTime.setText(SeTime);
    }

    public void setStopdis(String Stopdis) {
        tvStopdis.setText(Stopdis);
    }

    public void setRemainTime(String RemainTime) {
        tvRemainTime.setText(RemainTime);
    }

    public void setDistance(String Distance) {
        tvDistance.setText(Distance);
    }

    public void setPlate(String Plate) {
        tvPlate.setText(Plate);
    }


    public void FLOG(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        // TODO: 2018/12/22 切换bus方向需求待评估 

    }
}
