package com.fitz.abus.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fitz.abus.R;
import com.fitz.abus.activity.BusStopListActivity;
import com.fitz.abus.bean.ArriveBaseBean;
import com.fitz.abus.bean.BusBaseSHBean;
import com.fitz.abus.bean.BusLineDB;
import com.fitz.abus.fitzview.FitzSlideItemLayout;
import com.fitz.abus.utils.FitzDBUtils;
import com.fitz.abus.utils.FitzHttpUtils;
import com.fitz.abus.utils.MessageEvent;
import com.fitz.abus.utils.OnSlideItemTouch;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.adapter
 * @ClassName: FragmentListAdapter
 * @Author: Fitz
 * @CreateDate: 2019/1/6 16:37
 */
public class FragmentListAdapter extends RecyclerView.Adapter<FragmentListAdapter.MainViewHolder>
        implements OnSlideItemTouch.theItem {

    private static final String TAG = "FragmentListAdapter";
    private final int QMUI_DIAGLOG_STYLE = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    protected ArriveBaseBean arriveBaseBean;
    private Context mcontext;
    private List<BusLineDB> mList;
    private MainViewHolder mainViewHolder;
    private FitzHttpUtils.AbstractHttpCallBack mMainCallBack;
    private static QMUITipDialog tipDialog;

    public FragmentListAdapter(Context context, List<BusLineDB> list) {
        mcontext = context;
        mList = list;
        mMainCallBack = new FitzHttpUtils.AbstractHttpCallBack() {
            @Override
            public void onCallBefore() {
                super.onCallBefore();
                tipDialog = new QMUITipDialog.Builder(mcontext)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("查询中")
                        .create();
                tipDialog.show();
            }

            @Override
            public void onCallSuccess(String data) {
                super.onCallSuccess(data);
                tipDialog.dismiss();
                mainViewHolder.item_Detials.setVisibility(View.VISIBLE);
                mainViewHolder.ibSearch.setImageDrawable(mcontext.getDrawable(R.drawable.search_up));
                arriveBaseBean = new Gson().fromJson(data, ArriveBaseBean.class);
                mainViewHolder.setPlate(arriveBaseBean.getCars().get(0).getTerminal());
                mainViewHolder.setDistance(arriveBaseBean.getCars().get(0).getDistance());
                mainViewHolder.setRemainTime(arriveBaseBean.getCars().get(0).getTime());
                mainViewHolder.setStopdis(arriveBaseBean.getCars().get(0).getStopdis());
            }

            @Override
            public void onCallError(String meg) {
                super.onCallError(meg);
                tipDialog.dismiss();
                mainViewHolder.item_Detials.setVisibility(View.GONE);
                Toast.makeText(mcontext, "等待发车", Toast.LENGTH_SHORT).show();
                mainViewHolder.ibSearch.setImageDrawable(mcontext.getDrawable(R.drawable.search_down));
            }
        };
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item, viewGroup, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder mainViewHolder, final int i) {
        final BusLineDB currentBusLineDB = mList.get(i);
        Log.d(TAG, "the " + i + " currentBusLineDB:" + currentBusLineDB.toString());
        if (currentBusLineDB != null) {
            mainViewHolder.setTvLineName(currentBusLineDB.getLineName());
            mainViewHolder.setStationName(currentBusLineDB.getStationName());
            mainViewHolder.setSETime(
                    currentBusLineDB.getStartEarlyTime() + " - " + currentBusLineDB.getStartLateTime());
            mainViewHolder.setStartStop(currentBusLineDB.getStartStop());
            mainViewHolder.setEndStop(currentBusLineDB.getEndStop());
        }
        mainViewHolder.tvLineName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"tvLineName click");
                Intent intent = new Intent(mcontext, BusStopListActivity.class);
                BusBaseSHBean busBaseSHBean = new BusBaseSHBean();
                busBaseSHBean.setLine_name(currentBusLineDB.getLineName());
                busBaseSHBean.setLineId(currentBusLineDB.getLineID());
                busBaseSHBean.setStartStop(currentBusLineDB.getStartStop());
                busBaseSHBean.setEnd_stop(currentBusLineDB.getEndStop());
                busBaseSHBean.setStartEarlytime(currentBusLineDB.getStartEarlyTime());
                busBaseSHBean.setStartLatetime(currentBusLineDB.getStartLateTime());
                busBaseSHBean.setEndEarlytime(currentBusLineDB.getEndEarlyTime());
                busBaseSHBean.setEndLatetime(currentBusLineDB.getEndLateTime());

                Bundle b = new Bundle();
                b.putParcelable("busbaseSH", busBaseSHBean);
                intent.putExtras(b);
                mcontext.startActivity(intent);
            }
        });

        mainViewHolder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FitzDBUtils.getInstance().deleteBus(currentBusLineDB);
                EventBus.getDefault().post(new MessageEvent("item_delete"));
            }
        });

        mainViewHolder.item_Detials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"item_Detials click");
                httpQuest(currentBusLineDB);
            }
        });

        mainViewHolder.ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"ibSearch click");
            }
        });

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void itemSearchCilcked(MainViewHolder mainViewHolder) {
        Log.d(TAG,"itemSearchCilcked click");
        if(mainViewHolder.showSearch){
            this.mainViewHolder = mainViewHolder;
            BusLineDB b = mList.get(mainViewHolder.getAdapterPosition());
            httpQuest(b);
        }else {
            mainViewHolder.item_Detials.setVisibility(View.GONE);
            mainViewHolder.ibSearch.setImageDrawable(mcontext.getDrawable(R.drawable.search_down));
        }
        mainViewHolder.showSearch = !mainViewHolder.showSearch;
    }

    @Override
    public void itemDeleted(MainViewHolder mainViewHolder) {
        this.mainViewHolder = mainViewHolder;
        BusLineDB b = mList.get(mainViewHolder.getAdapterPosition());
        Log.d(TAG, "item_delete this:" + b.toString());
        FitzDBUtils.getInstance().deleteBus(b);
        EventBus.getDefault().post(new MessageEvent("item_delete"));
    }

    private void httpQuest(BusLineDB b){
        new FitzHttpUtils().getArriveBaseSH(b.getLineName(), b.getLineID(), b.getStationID(), b.getDirection(), mMainCallBack);
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        public FitzSlideItemLayout root;
        protected TextView tvLineName;
        protected TextView tvStationName;
        protected TextView tvStartStop;
        protected TextView tvEndStop;
        protected TextView tvSeTime;
        protected TextView tvStopdis;
        protected TextView tvRemainTime;
        protected TextView tvDistance;
        protected TextView tvPlate;
        protected View item_Detials;
        protected ViewGroup item_content;
        protected ImageButton item_delete;
        protected ImageButton ibSearch;
        protected boolean showSearch = true;

        public MainViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.item_root);
            tvLineName = v.findViewById(R.id.tv_line_name);
            tvStationName = v.findViewById(R.id.tv_stationName);
            tvStartStop = v.findViewById(R.id.tv_start_stop);
            tvEndStop = v.findViewById(R.id.tv_end_stop);
            tvSeTime = v.findViewById(R.id.tv_seTime);
            tvStopdis = v.findViewById(R.id.tv_stopdis);
            tvRemainTime = v.findViewById(R.id.tv_remainTime);
            tvDistance = v.findViewById(R.id.tv_distance);
            tvPlate = v.findViewById(R.id.tv_plate);
            item_Detials = v.findViewById(R.id.arrival);
            item_content = v.findViewById(R.id.item_content);
            item_delete = v.findViewById(R.id.item_delete);
            ibSearch = v.findViewById(R.id.item_search);
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

        public View getItem_delete(){
            return item_delete;
        }

        public ViewGroup getItem_content(){
            return item_content;
        }

        public ImageButton getSearch(){
            return ibSearch;
        }
    }
}
