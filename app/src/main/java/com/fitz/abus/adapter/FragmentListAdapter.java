package com.fitz.abus.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;
import com.fitz.abus.activity.BusStopListActivity;
import com.fitz.abus.bean.ArriveBaseSHBean;
import com.fitz.abus.bean.ArriveInfoWHBean;
import com.fitz.abus.bean.BusBaseInfoDB;
import com.fitz.abus.fitzview.FitzSlideItemLayout;
import com.fitz.abus.utils.FitzDBUtils;
import com.fitz.abus.utils.FitzHttpUtils;
import com.fitz.abus.utils.MessageEvent;
import com.fitz.abus.utils.OnSlideItemTouch;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.adapter
 * @ClassName: FragmentListAdapter
 * @Author: Fitz
 * @CreateDate: 2019/1/6 16:37
 */
public class FragmentListAdapter extends RecyclerView.Adapter<FragmentListAdapter.MainViewHolder> implements OnSlideItemTouch.theItem {

    public static final int MSG_REFRESH = 1;
    private static final String TAG = "fitzFragmentListAdapter";
    private static final Boolean DEBUG = true;
    protected TimerThread timerThread;
    private Context context;
    private List<BusBaseInfoDB> mList;
    private FitzHttpUtils.AbstractHttpCallBack mMainCallBack;
    private Handler handler;
    private volatile Map<MainViewHolder, TimerThread> holderThreadMap;

    public FragmentListAdapter(final Context context, List<BusBaseInfoDB> list) {
        this.context = context;
        mList = list;
        timerThread = new TimerThread();
        holderThreadMap = new HashMap<>();
        handler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);
                Object[] objs = (Object[]) msg.obj;
                switch (msg.what) {
                    case MSG_REFRESH:
                        httpQuest((MainViewHolder) objs[0], (BusBaseInfoDB) objs[1]);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder mainViewHolder, final int i) {
        final BusBaseInfoDB busBaseInfoDB = mList.get(i);
        Log.d(TAG, "the " + i + " currentBusBaseInfoDB:" + busBaseInfoDB.toString());
        if (busBaseInfoDB != null) {
            mainViewHolder.setTvLineName(busBaseInfoDB.getBusName());
            mainViewHolder.setStationName(busBaseInfoDB.getStationName());
            mainViewHolder.setSETime(busBaseInfoDB.getStartEndTimeDirection0());
            if(busBaseInfoDB.getDirection() == 0){
                mainViewHolder.setStartStop(busBaseInfoDB.getStartStopDirection0());
                mainViewHolder.setEndStop(busBaseInfoDB.getEndStopDirection0());
            }else {
                mainViewHolder.setStartStop(busBaseInfoDB.getStartStopDirection1());
                mainViewHolder.setEndStop(busBaseInfoDB.getEndStopDirection1());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void itemSearchCilcked(MainViewHolder mainViewHolder) {
        Log.d(TAG, "itemSearchCilcked click");
        mainViewHolder.setCallback_state(View.GONE);
        if (mainViewHolder.showSearch) {
            mainViewHolder.showSearch = false;
            mainViewHolder.item_Detials.setVisibility(View.VISIBLE);
            mainViewHolder.ibSearch.setImageDrawable(context.getDrawable(R.drawable.search_up));
            if (holderThreadMap.get(mainViewHolder) == null || !holderThreadMap.get(mainViewHolder).isAlive()) {
                TimerThread timerThread = new TimerThread(mainViewHolder, mList.get(mainViewHolder.getAdapterPosition()));
                timerThread.start();
                holderThreadMap.put(mainViewHolder, timerThread);
                FLOG("新建查询 " + mList.get(mainViewHolder.getLayoutPosition()).toString());
            } else {
                FLOG("重新查询 " + mList.get(mainViewHolder.getLayoutPosition()).toString());
                holderThreadMap.get(mainViewHolder).setReFresh(true);
            }
        } else {
            mainViewHolder.item_Detials.setVisibility(View.INVISIBLE);
            if (holderThreadMap.get(mainViewHolder) != null) {
                holderThreadMap.get(mainViewHolder).setReFresh(false);
            }
            mainViewHolder.showSearch = true;
            mainViewHolder.item_Detials.setVisibility(View.GONE);
            mainViewHolder.ibSearch.setImageDrawable(context.getDrawable(R.drawable.search_down));
        }
    }

    @Override
    public void itemDeleted(MainViewHolder mainViewHolder) {
        BusBaseInfoDB b = mList.get(mainViewHolder.getAdapterPosition());
        Log.d(TAG, "item_delete this:" + b.toString());
        FitzDBUtils.getInstance().deleteBus(b);
        EventBus.getDefault().post(new MessageEvent("item_delete"));
        if (holderThreadMap.get(mainViewHolder) != null) {
            holderThreadMap.get(mainViewHolder).setReFresh(false);
        }
        notifyItemRemoved(mainViewHolder.getAdapterPosition());
    }

    @Override
    public void itemLineName(MainViewHolder mainViewHolder) {
        BusBaseInfoDB b = mList.get(mainViewHolder.getAdapterPosition());
        Log.d(TAG, "itemLineName BusBaseInfoDB:" + b.toString());
        Intent intent = new Intent(context, BusStopListActivity.class);
        intent.putExtras(busLineDB2Bundle(b));
        context.startActivity(intent);
    }

    private Bundle busLineDB2Bundle(BusBaseInfoDB busBaseInfoDB) {
        Bundle bundle = new Bundle();
        switch (FitzApplication.getInstance().getDefaultCityCode()) {
            case FitzApplication.city_code_SH:
                bundle.putParcelable(BusStopListActivity.EXTRAS_BBI_SH, busBaseInfoDB);
                break;
            case FitzApplication.city_code_WH:
                bundle.putParcelable(BusStopListActivity.EXTRAS_BBI_WH, busBaseInfoDB);
                break;
            case FitzApplication.city_code_NJ:
                break;

            default:

                break;
        }
        return bundle;
    }

    private void startTimerThread(MainViewHolder mainViewHolder){
        Log.d(TAG, "startTimerThread");
        if (mainViewHolder.item_Detials.getVisibility() == View.VISIBLE) {
            if (holderThreadMap.get(mainViewHolder) == null || !holderThreadMap.get(mainViewHolder).isAlive()) {
                TimerThread timerThread = new TimerThread(mainViewHolder, mList.get(mainViewHolder.getAdapterPosition()));
                timerThread.start();
                holderThreadMap.put(mainViewHolder, timerThread);
                FLOG("新建查询 " + mList.get(mainViewHolder.getLayoutPosition()).toString());
            } else {
                FLOG("重新查询 " + mList.get(mainViewHolder.getLayoutPosition()).toString());
                holderThreadMap.get(mainViewHolder).setReFresh(true);
            }
        }
    }

    private void httpQuest(final MainViewHolder mainViewHolder, BusBaseInfoDB b) {
        Log.d(TAG, "BusBaseInfoDB:" + b.toString());
        mMainCallBack = new FitzHttpUtils.AbstractHttpCallBack() {
            @Override
            public void onCallBefore() {
                super.onCallBefore();
                // TODO: 2019/1/18 添加刷新动画
                mainViewHolder.setCallback_state(View.GONE);
                mainViewHolder.setCallback_loading(View.VISIBLE);
            }

            @Override
            public void onCallSuccess(String data) {
                super.onCallSuccess(data);
                mainViewHolder.setCallback_state(View.VISIBLE);
                mainViewHolder.callback_state.setImageDrawable(context.getResources().getDrawable(R.drawable.call_success,null));
                mainViewHolder.setCallback_loading(View.INVISIBLE);
                if (mainViewHolder == null) {
                    return;
                }
                switch (FitzApplication.getInstance().getDefaultCityCode()) {
                    case FitzApplication.city_code_SH:
                        ArriveBaseSHBean arriveBaseSHBean = new Gson().fromJson(data, ArriveBaseSHBean.class);
                        mainViewHolder.setPlate(arriveBaseSHBean.getCars().get(0).getTerminal());
                        mainViewHolder.setDistance(arriveBaseSHBean.getCars().get(0).getDistance() + "米");
                        mainViewHolder.setStopdis(arriveBaseSHBean.getCars().get(0).getStopdis() + "站");
                        break;
                    case FitzApplication.city_code_WH:
                        ArriveInfoWHBean arriveInfoWHBean = new Gson().fromJson(data, ArriveInfoWHBean.class);
                        mainViewHolder.setPlate(arriveInfoWHBean.getResult().getPlate());
                        mainViewHolder.setDistance(arriveInfoWHBean.getResult().getDistance());
                        mainViewHolder.setStopdis(arriveInfoWHBean.getResult().getWillArriveTime());
                        break;
                    case FitzApplication.city_code_NJ:
                        break;
                    default:
                        return;
                }
            }

            @Override
            public void onCallError(String meg) {
                super.onCallError(meg);
                // TODO: 2019/1/18 等待发车状态显示
                mainViewHolder.setCallback_state(View.VISIBLE);
                mainViewHolder.callback_state.setImageDrawable(context.getResources().getDrawable(R.drawable.call_error,null));
                mainViewHolder.setCallback_loading(View.INVISIBLE);
                mainViewHolder.setPlate("");
                mainViewHolder.setDistance(context.getResources().getString(R.string.waiting));
                mainViewHolder.setStopdis("");
            }
        };
        switch (FitzApplication.getInstance().getDefaultCityCode()) {
            case FitzApplication.city_code_SH:
                new FitzHttpUtils().getArriveBaseSH(b.getBusName(), b.getLineId(), b.getStationID(), b.getDirection(), mMainCallBack);
                break;
            case FitzApplication.city_code_WH:
                new FitzHttpUtils().postArriveBaseWH(b.getBusName(), b.getStationID(), b.getDirection(), mMainCallBack);
                break;
            case FitzApplication.city_code_NJ:
                break;
            default:
                return;
        }

    }

    public void FLOG(String s) {
        if (DEBUG) {
            Log.d(TAG, s);
        }
    }

    /**
     * 切换 fragment 后一定要中断已开启的线程
     */
    public void release() {
        FLOG("release");
        Set<MainViewHolder> keys = holderThreadMap.keySet();
        Iterator<MainViewHolder> iterator = keys.iterator();
        while (iterator.hasNext()) {
            holderThreadMap.get(iterator.next()).setReFresh(false);
        }
    }

    /**
     * fragment从后台回复后，重新开启打开的线程
     * */
    public void resume(){
        FLOG("resume");
        Set<MainViewHolder> keys = holderThreadMap.keySet();
        Iterator<MainViewHolder> iterator = keys.iterator();
        while (iterator.hasNext()) {
            startTimerThread(iterator.next());
        }
    }

    public class TimerThread extends Thread {

        protected BusBaseInfoDB busBaseInfoDB;
        protected MainViewHolder mainViewHolder;
        private Boolean reFresh = true;

        public TimerThread() {
        }

        public TimerThread(MainViewHolder mainViewHolder, BusBaseInfoDB busBaseInfoDB) {
            this.busBaseInfoDB = busBaseInfoDB;
            this.mainViewHolder = mainViewHolder;
        }

        public Boolean getReFresh() {
            return reFresh;
        }

        public void setReFresh(Boolean reFresh) {
            this.reFresh = reFresh;
            FLOG("setReFresh：" + reFresh);
        }

        public void bind(MainViewHolder mainViewHolder, BusBaseInfoDB busBaseInfoDB) {
            this.busBaseInfoDB = busBaseInfoDB;
            this.mainViewHolder = mainViewHolder;
        }

        @Override
        public void run() {
            super.run();
            while (reFresh) {
                try {
                    FLOG("5s循环查询");
                    Message msg = new Message();
                    msg.what = MSG_REFRESH;
                    msg.obj = new Object[]{mainViewHolder, busBaseInfoDB};
                    handler.sendMessage(msg);
                    Thread.sleep(FitzApplication.getInstance().getRefreshTime());
                    reFresh = reFresh && FitzApplication.getInstance().isAutoRefresh();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            FLOG("停止查询" + (busBaseInfoDB == null ? "" : busBaseInfoDB.toString()));
            interrupt();
        }
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        public FitzSlideItemLayout root;
        protected TextView tvLineName;
        protected TextView tvStationName;
        protected TextView tvStartStop;
        protected TextView tvEndStop;
        protected TextView tvSeTime;
        protected TextView tvStopdis;
        protected TextView tvDistance;
        protected TextView tvPlate;
        protected ViewGroup item_Detials;
        protected ViewGroup item_content;
        protected ImageButton item_delete;
        protected ImageButton ibSearch;
        protected boolean showSearch = true;
        protected View callback_loading;
        protected ImageView callback_state;

        public MainViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.item_root);
            tvLineName = v.findViewById(R.id.tv_line_name);
            tvStationName = v.findViewById(R.id.tv_stationName);
            tvStartStop = v.findViewById(R.id.tv_start_stop);
            tvEndStop = v.findViewById(R.id.tv_end_stop);
            tvSeTime = v.findViewById(R.id.tv_seTime);
            tvStopdis = v.findViewById(R.id.tv_stopdis);
            tvDistance = v.findViewById(R.id.tv_distance);
            tvPlate = v.findViewById(R.id.tv_plate);
            item_Detials = v.findViewById(R.id.arrival);
            item_content = v.findViewById(R.id.item_content);
            item_delete = v.findViewById(R.id.item_delete);
            ibSearch = v.findViewById(R.id.item_search);
            callback_loading = v.findViewById(R.id.empty_view_loading);
            callback_state = v.findViewById(R.id.callback_state);
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

        public void setDistance(String Distance) {
            tvDistance.setText(Distance);
        }

        public void setPlate(String Plate) {
            tvPlate.setText(Plate);
        }

        public TextView getTvLineName() {
            return tvLineName;
        }

        public void setTvLineName(String LineName) {
            tvLineName.setText(LineName);
        }

        public View getItem_delete() {
            return item_delete;
        }

        public ViewGroup getItem_content() {
            return item_content;
        }

        public ImageButton getSearch() {
            return ibSearch;
        }

        public ViewGroup getItem_Detials() {
            return item_Detials;
        }

        public void setCallback_loading(int vis) {
            callback_loading.setVisibility(vis);
        }

        public void setCallback_state(int vis) {
            callback_state.setVisibility(vis);
        }
    }
}
