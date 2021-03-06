package com.fitz.abus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.fitz.abus.bean.ArriveBaseSHBean;
import com.fitz.abus.bean.ArriveBusInfo;
import com.fitz.abus.bean.ArriveInfoWHBean;
import com.fitz.abus.bean.BusBaseInfoDB;
import com.fitz.abus.utils.FitzDBUtils;
import com.fitz.abus.utils.FitzHttpUtils;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.adapter
 * @ClassName: StopListRecycleAdapter
 * @Author: Fitz
 * @CreateDate: 2018/12/23 17:34
 */

public class StopListRecycleAdapter extends RecyclerView.Adapter<StopListRecycleAdapter.StationViewHolder> {

    private static final String TAG = "fitzStopListRecycleAdapter";
    private static final int FAST_CLICK_DELAY_TIME = 1000;
    private static QMUITipDialog tipDialog;
    private final int QMUI_DIAGLOG_STYLE = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    protected ArriveBaseSHBean arriveBaseSHBean;
    protected ArriveInfoWHBean arriveInfoWHBean;
    private List<ArriveBusInfo> arriveBusInfoList;
    private Context context;
    private BusBaseInfoDB busBaseInfoDB;
    private int selectedIndex = -1;
    private FitzHttpUtils.AbstractHttpCallBack mArriveBaseCallBack;
    private long lastClickTime = 0L;


    public StopListRecycleAdapter(final Context context, final BusBaseInfoDB busBaseInfoDB, List<ArriveBusInfo> list) {
        this.context = context;
        this.busBaseInfoDB = busBaseInfoDB;
        arriveBusInfoList = list;

        mArriveBaseCallBack = new FitzHttpUtils.AbstractHttpCallBack() {
            @Override
            public void onCallBefore() {
                super.onCallBefore();
                tipDialog = new QMUITipDialog.Builder(StopListRecycleAdapter.this.context).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                                                                          .setTipWord("查询中").create();
                tipDialog.show();
            }

            @Override
            public void onCallSuccess(String data) {
                super.onCallSuccess(data);
                Log.d(TAG, data);
                tipDialog.dismiss();
                String plate = "";
                String distance = "";
                String remainingStation = "";
                switch (FitzApplication.getInstance().getDefaultCityCode()) {
                    case FitzApplication.city_code_SH:
                        arriveBaseSHBean = new Gson().fromJson(data, ArriveBaseSHBean.class);
                        plate = arriveBaseSHBean.getCars().get(0)
                                                .getTerminal();
                        distance = arriveBaseSHBean
                                .getCars().get(0)
                                .getDistance();
                        remainingStation = arriveBaseSHBean
                                .getCars()
                                .get(0)
                                .getStopdis();
                        break;
                    case FitzApplication.city_code_WH:
                        arriveInfoWHBean = new Gson().fromJson(data, ArriveInfoWHBean.class);
                        plate = arriveInfoWHBean.getResult().getPlate();
                        distance = arriveInfoWHBean.getResult().getDistance();
                        remainingStation = arriveInfoWHBean.getResult().getWillArriveTime();
                        //获取数字
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(remainingStation);
                        while (matcher.find()) {
                            remainingStation = matcher.group(0);
                        }
                        break;
                    case FitzApplication.city_code_NJ:
                        break;
                    default:
                }
                String message = context.getResources()
                                        .getString(R.string.plate) + plate + "\n" +
                        context.getResources()
                               .getString(R.string.distance) +
                        distance + "\n" + context
                        .getResources()
                        .getString(R.string.remaining) +
                        remainingStation + (Integer.parseInt
                        (remainingStation) > 2 ? context
                        .getResources()
                        .getString(R.string.stations) : context.getResources()
                                                               .getString(R.string.station));

                new QMUIDialog.MessageDialogBuilder(StopListRecycleAdapter.this.context).setTitle(busBaseInfoDB.getStationName())
                                                                                        .setMessage(message)
                                                                                        .addAction(context.getResources()
                                                                                                          .getString(R.string.cancel), new
                                                                                                           QMUIDialogAction.ActionListener() {
                                                                                                               @Override
                                                                                                               public void onClick(QMUIDialog
                                                                                                                                           dialog,
                                                                                                                                   int index) {
                                                                                                                   dialog.dismiss();
                                                                                                               }
                                                                                                           })
                                                                                        .addAction(context.getResources()
                                                                                                          .getString(R.string.save), new
                                                                                                           QMUIDialogAction.ActionListener() {
                                                                                                               @Override
                                                                                                               public void onClick(QMUIDialog
                                                                                                                                           dialog,
                                                                                                                                   int index) {
                                                                                                                   dialog.dismiss();
                                                                                                                   saveBus();
                                                                                                                   Toast.makeText
                                                                                                                           (StopListRecycleAdapter
                                                                                                                                    .this.context,
                                                                                                                            "已收藏此站", Toast
                                                                                                                                    .LENGTH_SHORT)
                                                                                                                        .show();
                                                                                                               }
                                                                                                           })
                                                                                        .create(QMUI_DIAGLOG_STYLE)
                                                                                        .show();
            }

            @Override
            public void onCallError(String meg) {
                super.onCallError(meg);
                tipDialog.dismiss();
                new QMUIDialog.MessageDialogBuilder(StopListRecycleAdapter.this.context).setTitle(busBaseInfoDB.getStationName()).setMessage("等待发车")
                                                                                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                                                                                            @Override
                                                                                            public void onClick(QMUIDialog dialog, int index) {
                                                                                                dialog.dismiss();
                                                                                            }
                                                                                        }).addAction("收藏", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        saveBus();
                        Toast.makeText(StopListRecycleAdapter.this.context, "已收藏此站", Toast.LENGTH_SHORT).show();
                    }
                }).create(QMUI_DIAGLOG_STYLE).show();
            }
        };
    }

    private void saveBus() {
        busBaseInfoDB.setCityID(FitzApplication.getInstance().getDefaultCityCode());
        FitzDBUtils.getInstance().insertBus(busBaseInfoDB);
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stop_list_item, viewGroup, false);
        return new StationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final StationViewHolder holder, final int i) {
        final String currentStopName = arriveBusInfoList.get(i).getBusName();
        final String stopId = arriveBusInfoList.get(i).getStationId();
        Log.d(TAG, "currentStopName:" + currentStopName + " stopId:" + stopId);
        holder.stop_name.setText((i + 1) + " " + currentStopName);
        if (selectedIndex == i) {
            holder.checkButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.circle_selected));
        } else {
            holder.checkButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.circle_unselected));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                //更新选中状态
                setSelectedIndex(i);
                int direction = FitzApplication.direction ? 0 : 1;
                busBaseInfoDB.setStationName(currentStopName);
                busBaseInfoDB.setStationID(stopId);
                busBaseInfoDB.setDirection(direction);
                startNetQuest(busBaseInfoDB);
            }
        });
    }

    private void startNetQuest(BusBaseInfoDB busBaseInfoDB) {
        Log.d(TAG,"startNetQuest:"+busBaseInfoDB.toString());
        switch (FitzApplication.getInstance().getDefaultCityCode()) {
            case FitzApplication.city_code_SH:
                FitzHttpUtils.getInstance().getArriveBaseSH(busBaseInfoDB.getBusName(), busBaseInfoDB.getLineId(), busBaseInfoDB
                        .getStationID(), busBaseInfoDB.getDirection(), mArriveBaseCallBack);
                break;
            case FitzApplication.city_code_WH:
                FitzHttpUtils.getInstance().postArriveBaseWH(busBaseInfoDB.getBusName(), busBaseInfoDB.getStationID(), busBaseInfoDB.getDirection(), mArriveBaseCallBack);
                break;
            case FitzApplication.city_code_NJ:
                break;
            default:

                break;
        }
    }

    public void setSelectedIndex(int position) {
        this.selectedIndex = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arriveBusInfoList.size();

    }

    public class StationViewHolder extends RecyclerView.ViewHolder {
        TextView stop_name;
        ImageButton checkButton;

        public StationViewHolder(View v) {
            super(v);
            stop_name = v.findViewById(R.id.stop_name);
            checkButton = v.findViewById(R.id.stop_check_state);
        }
    }

}
