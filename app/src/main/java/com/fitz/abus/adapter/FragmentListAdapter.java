package com.fitz.abus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fitz.abus.R;
import com.fitz.abus.bean.ArriveBaseBean;
import com.fitz.abus.bean.BusLineDB;
import com.fitz.abus.utils.FitzDBUtils;
import com.fitz.abus.utils.FitzHttpUtils;
import com.fitz.abus.utils.MessageEvent;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.adapter
 * @ClassName: FragmentListAdapter
 * @Author: Fitz
 * @CreateDate: 2019/1/6 16:37
 */
public class FragmentListAdapter extends RecyclerView.Adapter<FragmentListAdapter.MainViewHolder> {

    private static final String TAG = "FragmentListAdapter";
    private final int QMUI_DIAGLOG_STYLE = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    protected ArriveBaseBean arriveBaseBean;
    private Context mcontext;
    private List<BusLineDB> mList;
    private BusLineDB currentBusLineDB;
    private FitzHttpUtils.AbstractHttpCallBack mMainCallBack;

    public FragmentListAdapter(Context context, List<BusLineDB> list) {
        mcontext = context;
        mList = list;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_card, viewGroup, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder mainViewHolder, int i) {
        currentBusLineDB = mList.get(i);
        Log.d(TAG, "the " + i + " currentBusLineDB:" + currentBusLineDB.toString());
        if (currentBusLineDB != null) {
            mainViewHolder.setTvLineName(currentBusLineDB.getLineName());
            mainViewHolder.setStationName(currentBusLineDB.getStationName());
            mainViewHolder.setSETime(currentBusLineDB.getStartTime() + " - " + currentBusLineDB.getEndTime());
            mainViewHolder.setStartStop(currentBusLineDB.getStartStop());
            mainViewHolder.setEndStop(currentBusLineDB.getEndStop());
            mMainCallBack = new FitzHttpUtils.AbstractHttpCallBack() {
                @Override
                public void onCallBefore() {
                    super.onCallBefore();
                }

                @Override
                public void onCallSuccess(String data) {
                    super.onCallSuccess(data);
                    arriveBaseBean = new Gson().fromJson(data, ArriveBaseBean.class);
                    mainViewHolder.setPlate(arriveBaseBean.getCars().get(0).getTerminal());
                    mainViewHolder.setDistance(arriveBaseBean.getCars().get(0).getDistance());
                    mainViewHolder.setRemainTime(arriveBaseBean.getCars().get(0).getTime());
                    mainViewHolder.setStopdis(arriveBaseBean.getCars().get(0).getStopdis());
                }

                @Override
                public void onCallError(String meg) {
                    super.onCallError(meg);
                    //Toast.makeText(FitzBusCard.this.context, "等待发车", Toast.LENGTH_SHORT).show();
                }
            };
            new FitzHttpUtils().getArriveBaseSH(currentBusLineDB.getLineName(), currentBusLineDB.getLineID(), currentBusLineDB.getStationID(),
                    currentBusLineDB.getDirection(), mMainCallBack);
        }
        mainViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FitzHttpUtils().getArriveBaseSH(currentBusLineDB.getLineName(), currentBusLineDB.getLineID(), currentBusLineDB.getStationID(),
                        currentBusLineDB.getDirection(), mMainCallBack);
            }
        });

        mainViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new QMUIDialog.MessageDialogBuilder(mcontext)
                        .setTitle("取消收藏？")
                        .setMessage("取消收藏后主页将不再显示，您可以重新查询后收藏")
                        .addAction("返回", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0, "取消收藏", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                delete(currentBusLineDB);
                                EventBus.getDefault().post(new MessageEvent("delete"));
                            }
                        })
                        .create(QMUI_DIAGLOG_STYLE)
                        .show();
                return false;
            }
        });
    }

    private void delete(BusLineDB b) {
        Log.d(TAG, "delete this:" + b.toString());
        FitzDBUtils.getInstance().deleteBus(b);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvLineName;
        protected TextView tvStationName;
        protected TextView tvStartStop;
        protected TextView tvEndStop;
        protected TextView tvSeTime;
        protected TextView tvStopdis;
        protected TextView tvRemainTime;
        protected TextView tvDistance;
        protected TextView tvPlate;

        public MainViewHolder(View v) {
            super(v);
            tvLineName = v.findViewById(R.id.tv_line_name);
            tvStationName = v.findViewById(R.id.tv_stationName);
            tvStartStop = v.findViewById(R.id.tv_start_stop);
            tvEndStop = v.findViewById(R.id.tv_end_stop);
            tvSeTime = v.findViewById(R.id.tv_seTime);
            tvStopdis = v.findViewById(R.id.tv_stopdis);
            tvRemainTime = v.findViewById(R.id.tv_remainTime);
            tvDistance = v.findViewById(R.id.tv_distance);
            tvPlate = v.findViewById(R.id.tv_plate);
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
    }

}
