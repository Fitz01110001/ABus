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
import android.widget.TextView;
import android.widget.Toast;

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
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.adapter
 * @ClassName: FragmentListAdapter
 * @Author: Fitz
 * @CreateDate: 2019/1/6 16:37
 */
public class FragmentListAdapter extends RecyclerView.Adapter<FragmentListAdapter.MainViewHolder> implements OnSlideItemTouch.theItem {

    public static final int MSG_REFRESH = 1;
    private static final String TAG = "FragmentListAdapter";
    private static QMUITipDialog tipDialog;
    private final int QMUI_DIAGLOG_STYLE = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private Context context;
    private TimerThread timerThread;
    private List<BusBaseInfoDB> mList;
    private static final Boolean DEBUG = true;
    private FitzHttpUtils.AbstractHttpCallBack mMainCallBack;
    private Handler handler;
    private volatile Map<MainViewHolder, TimerThread> holderThreadMap = new HashMap<MainViewHolder, TimerThread>();

    public FragmentListAdapter(final Context context, List<BusBaseInfoDB> list) {
        this.context = context;
        mList = list;
        timerThread = new TimerThread();
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
        tipDialog = new QMUITipDialog.Builder(this.context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(context.getResources().getString(R.string.TipWord))
                .create();

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
            mainViewHolder.setStartStop(busBaseInfoDB.getStartStopDirection0());
            mainViewHolder.setEndStop(busBaseInfoDB.getEndStopDirection0());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void itemSearchCilcked(MainViewHolder mainViewHolder) {
        Log.d(TAG, "itemSearchCilcked click");
        if (mainViewHolder.showSearch) {
            mainViewHolder.item_Detials.setVisibility(View.VISIBLE);
            if (holderThreadMap.get(mainViewHolder) == null) {
                TimerThread timerThread = new TimerThread(mainViewHolder, mList.get(mainViewHolder.getAdapterPosition()));
                holderThreadMap.put(mainViewHolder, timerThread);
                timerThread.start();
            } else if (!holderThreadMap.get(mainViewHolder).isAlive()) {
                // TODO: 2019/1/17 重启线程存在问题。
                holderThreadMap.get(mainViewHolder).setReFresh(true);
                holderThreadMap.get(mainViewHolder).run();
            }
            mainViewHolder.showSearch = false;
            //httpQuest(mainViewHolder, b);
        } else {

            mainViewHolder.item_Detials.setVisibility(View.INVISIBLE);
            if (holderThreadMap.get(mainViewHolder) != null) {

            }
            holderThreadMap.get(mainViewHolder).setReFresh(false);
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
    }

    @Override
    public void itemDetials(MainViewHolder mainViewHolder) {
        BusBaseInfoDB b = mList.get(mainViewHolder.getAdapterPosition());
        httpQuest(mainViewHolder, b);
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
        switch (FitzApplication.getInstance().getDefaultCityKey()) {
            case FitzApplication.keySH:
                bundle.putParcelable(BusStopListActivity.EXTRAS_BBI_SH, busBaseInfoDB);
                break;
            case FitzApplication.keyWH:
                bundle.putParcelable(BusStopListActivity.EXTRAS_BBI_WH, busBaseInfoDB);
                break;
            case FitzApplication.keyNJ:
                break;

            default:

                break;
        }
        return bundle;
    }

    private void httpQuest(final MainViewHolder mainViewHolder, BusBaseInfoDB b) {
        Log.d(TAG, "BusBaseInfoDB:" + b.toString());
        mMainCallBack = new FitzHttpUtils.AbstractHttpCallBack() {
            @Override
            public void onCallBefore() {
                super.onCallBefore();
                tipDialog.show();
            }

            @Override
            public void onCallSuccess(String data) {
                super.onCallSuccess(data);
                tipDialog.dismiss();
                //mainViewHolder.showSearch = false;
                mainViewHolder.ibSearch.setImageDrawable(FragmentListAdapter.this.context.getDrawable(R.drawable.search_up));
                switch (FitzApplication.getInstance().getDefaultCityKey()) {
                    case FitzApplication.keySH:
                        ArriveBaseSHBean arriveBaseSHBean = new Gson().fromJson(data, ArriveBaseSHBean.class);
                        mainViewHolder.setPlate(arriveBaseSHBean.getCars().get(0).getTerminal());
                        mainViewHolder.setDistance(arriveBaseSHBean.getCars().get(0).getDistance());
                        mainViewHolder.setRemainTime(arriveBaseSHBean.getCars().get(0).getTime());
                        mainViewHolder.setStopdis(arriveBaseSHBean.getCars().get(0).getStopdis());
                        break;
                    case FitzApplication.keyWH:
                        ArriveInfoWHBean arriveInfoWHBean = new Gson().fromJson(data, ArriveInfoWHBean.class);
                        mainViewHolder.setPlate(arriveInfoWHBean.getResult().getPlate());
                        mainViewHolder.setDistance(arriveInfoWHBean.getResult().getDistance());
                        mainViewHolder.setRemainTime("");
                        mainViewHolder.setStopdis(arriveInfoWHBean.getResult().getWillArriveTime());
                        break;
                    case FitzApplication.keyNJ:
                        break;
                    default:
                        return;
                }

            }

            @Override
            public void onCallError(String meg) {
                super.onCallError(meg);
                tipDialog.dismiss();
                //mainViewHolder.showSearch = true;
                //mainViewHolder.item_Detials.setVisibility(View.GONE);
                Toast.makeText(context, context.getResources().getString(R.string.waiting), Toast.LENGTH_SHORT).show();
                mainViewHolder.ibSearch.setImageDrawable(FragmentListAdapter.this.context.getDrawable(R.drawable.search_down));
            }
        };
        switch (FitzApplication.getInstance().getDefaultCityKey()) {
            case FitzApplication.keySH:
                new FitzHttpUtils().getArriveBaseSH(b.getBusName(), b.getLineId(), b.getStationID(), b.getDirection(), mMainCallBack);
                break;
            case FitzApplication.keyWH:
                new FitzHttpUtils().postArriveBaseWH(b.getBusName(), b.getStationID(), b.getDirection(), mMainCallBack);
                break;
            case FitzApplication.keyNJ:
                break;
            default:
                return;
        }

    }

    public class TimerThread extends Thread {

        protected BusBaseInfoDB busBaseInfoDB;
        protected MainViewHolder mainViewHolder;

        public Boolean getReFresh() {
            return reFresh;
        }

        public void setReFresh(Boolean reFresh) {
            this.reFresh = reFresh;
        }

        private Boolean reFresh = true;

        public TimerThread() {
        }

        public TimerThread(MainViewHolder mainViewHolder, BusBaseInfoDB busBaseInfoDB) {
            this.busBaseInfoDB = busBaseInfoDB;
            this.mainViewHolder = mainViewHolder;
        }

        public void bind(MainViewHolder mainViewHolder, BusBaseInfoDB busBaseInfoDB) {
            this.busBaseInfoDB = busBaseInfoDB;
            this.mainViewHolder = mainViewHolder;
        }

        @Override
        public void run() {
            super.run();
            do {
                try {
                    FLOG("5s循环查询");
                    Message msg = new Message();
                    msg.what = MSG_REFRESH;
                    msg.obj = new Object[]{mainViewHolder, busBaseInfoDB};
                    handler.sendMessage(msg);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (reFresh);
            FLOG("停止查询");
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
        protected TextView tvRemainTime;
        protected TextView tvDistance;
        protected TextView tvPlate;
        protected ViewGroup item_Detials;
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
    }

    public void FLOG(String s) {
        if (DEBUG) {
            Log.d(TAG, s);
        }
    }
}
