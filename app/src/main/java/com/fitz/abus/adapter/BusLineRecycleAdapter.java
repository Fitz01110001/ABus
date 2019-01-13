package com.fitz.abus.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.R;
import com.fitz.abus.activity.BusStopListActivity;
import com.fitz.abus.bean.BusBaseSHBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.adapter
 * @ClassName: BusLineRecycleAdapter
 * @Author: Fitz
 * @CreateDate: 2018/12/23 20:02
 */
public class BusLineRecycleAdapter extends RecyclerView.Adapter<BusLineRecycleAdapter.FitzViewHolder> {

    private Context context;
    private List<BusBaseSHBean> mListSh = new ArrayList<>();
    private List<String> mListWh = new ArrayList<>();

    /**
     * for sh
     */
    public BusLineRecycleAdapter(Context context, BusBaseSHBean busBaseSHBean) {
        this.context = context;
        if (busBaseSHBean.nonNull()) {
            mListSh.clear();
            mListSh.add(busBaseSHBean);
        }
    }

    /**
     * for wh
     */
    public BusLineRecycleAdapter(Context context, List<String> list) {
        this.context = context;
        mListWh = list;
    }


    @NonNull
    @Override
    public FitzViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.busline_recycleview_item, viewGroup, false);
        return new FitzViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FitzViewHolder holder, final int i) {
        holder.tv_line_name.setText(getLineName(i));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusStopListActivity.class);
                switch (FitzApplication.getInstance().getDefaultCityKey()) {
                    case FitzApplication.keySH:
                        Bundle b = new Bundle();
                        b.putParcelable(BusStopListActivity.EXTRAS_SH, mListSh.get(i));
                        intent.putExtras(b);
                        break;
                    case FitzApplication.keyWH:
                        intent.putExtra(BusStopListActivity.EXTRAS_WH,mListWh.get(i));
                        break;
                    case FitzApplication.keyNJ:
                        break;
                    default:
                        return;
                }
                context.startActivity(intent);
            }
        });
    }

    private String getLineName(int i){
        switch (FitzApplication.getInstance().getDefaultCityKey()) {
            case FitzApplication.keySH:
                return mListSh.get(i).getLine_name();
            case FitzApplication.keyWH:
                return mListWh.get(i);
            case FitzApplication.keyNJ:
                break;
            default:
                return null;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        switch (FitzApplication.getInstance().getDefaultCityKey()) {
            case FitzApplication.keySH:
                return mListSh.size();
            case FitzApplication.keyWH:
                return mListWh.size();
            case FitzApplication.keyNJ:
                break;
            default:
                return 0;
        }
        return 0;
    }

    public class FitzViewHolder extends RecyclerView.ViewHolder {
        TextView tv_line_name;
        TextView tv_end_stop;

        public FitzViewHolder(View v) {
            super(v);
            tv_line_name = v.findViewById(R.id.line_name);
            tv_end_stop = v.findViewById(R.id.end_stop);
        }
    }
}
