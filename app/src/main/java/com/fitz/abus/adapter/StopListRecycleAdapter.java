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
import com.fitz.abus.bean.ArriveBaseBean;
import com.fitz.abus.bean.BusBaseSHBean;
import com.fitz.abus.bean.Stops;
import com.fitz.abus.utils.FitzHttpUtils;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.adapter
 * @ClassName: StopListRecycleAdapter
 * @Author: Fitz
 * @CreateDate: 2018/12/23 17:34
 */

public class StopListRecycleAdapter extends RecyclerView.Adapter<StopListRecycleAdapter.StationViewHolder> {

    private static final String TAG = "fitzStopListRecycleAdapter";
    private Context mcontext;
    private List<Stops> mList;
    private int selectedIndex = -1;
    private String busName;
    private String lineId;
    private FitzHttpUtils.AbstractHttpCallBack mArriveBaseCallBack;
    protected ArriveBaseBean arriveBaseBean;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private String titleStopName;
    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 1000;

    public StopListRecycleAdapter(Context context, BusBaseSHBean busbaseSH, List<Stops> list) {
        mcontext = context;
        mList = list;
        this.busName = busbaseSH.getLine_name();
        this.lineId = busbaseSH.getLineId();
        mArriveBaseCallBack = new FitzHttpUtils.AbstractHttpCallBack() {
            @Override
            public void onCallBefore() {
                super.onCallBefore();
                // TODO: 2018/12/26 这里需要等待动画 
            }

            @Override
            public void onCallSuccess(String data) {
                super.onCallSuccess(data);
                Log.d(TAG,data);
                arriveBaseBean = new Gson().fromJson(data, ArriveBaseBean.class);
                new QMUIDialog.MessageDialogBuilder(mcontext)
                        .setTitle(titleStopName)
                        .setMessage("车牌:"+arriveBaseBean.getCars().get(0).getTerminal()+"\n"+
                                "距离:"+arriveBaseBean.getCars().get(0).getDistance()+"\n"+
                                "还有:"+arriveBaseBean.getCars().get(0).getStopdis()+"站")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("收藏", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                Toast.makeText(mcontext, "000", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create(mCurrentDialogStyle).show();
            }

            @Override
            public void onCallError(String meg) {
                super.onCallError(meg);
                // TODO: 2018/12/26 异常处理
                Toast.makeText(mcontext, "等待发车", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stop_list_item, viewGroup, false);
        return new StationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final StationViewHolder holder, final int i) {
        Log.d(TAG,"i:"+i);
        String currentStopName = mList.get(i).getZdmc();
        holder.stop_name.setText(currentStopName);
        if (selectedIndex == i) {
            holder.checkButton.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.circle_selected));
        } else {
            holder.checkButton.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.circle_unselected));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                //更新选中状态
                setSelectedIndex(i);
                titleStopName = mList.get(i).getZdmc();
                Log.d(TAG,"name:"+mList.get(i).getZdmc()+" id:"+mList.get(i).getId());
                int direction =  FitzApplication.directionSH ? 1 : 0;
                new FitzHttpUtils().getArriveBaseSH(busName, lineId, mList.get(i).getId(), direction, mArriveBaseCallBack);

            }
        });
    }

    public void setSelectedIndex(int position) {
        this.selectedIndex = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
