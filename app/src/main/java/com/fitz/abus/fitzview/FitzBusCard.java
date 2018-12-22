package com.fitz.abus.fitzview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.fitz.abus.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.fitzView
 * @ClassName: FitzBusCard
 * @Author: Fitz
 * @CreateDate: 2018/12/16 20:10
 */
public class FitzBusCard extends TableLayout {

    @BindView(R.id.tv_line_name)
    TextView tvLineName;
    @BindView(R.id.tv_stationId)
    TextView tvStationId;
    @BindView(R.id.tv_start_stop)
    TextView tvStartStop;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.tv_end_stop)
    TextView tvEndStop;
    @BindView(R.id.tv_seTime)
    TextView tvSeTime;
    @BindView(R.id.tv_stopdis)
    TextView tvStopdis;
    @BindView(R.id.tv_remainTime)
    TextView tvRemainTime;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_plate)
    TextView tvPlate;


    private boolean isDebug = true;
    private String TAG = "FitzBusCard";
    private Context mContext;
    private View contentView;

    public FitzBusCard(Context context) {
        super(context);
        mContext = context;
        initBusCard();
    }

    private void initBusCard() {
        contentView = inflate(mContext, R.layout.bus_info_card, this);

    }

    public void setTvLineName(String LineName) {
        tvLineName.setText(LineName);
    }

    public void setStationID(String StationID) {
        tvStationId.setText(StationID);
    }

    public void setStartStop(String StartStop) {
        tvStartStop.setText(StartStop);
    }

    public void setEndStop(String EndStop) {
        tvEndStop.setText(EndStop);
    }

    public void setSeTime(String SeTime) {
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
