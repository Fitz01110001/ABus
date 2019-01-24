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
import java.util.Random;

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
    private List<Integer> heightList;

    /**
     * for sh
     */
    public BusLineRecycleAdapter(Context context, BusBaseSHBean busBaseSHBean) {
        this.context = context;
        if (busBaseSHBean.nonNull()) {
            mListSh.clear();
            mListSh.add(busBaseSHBean);
        }
        initHighList(mListSh.size());
    }

    /**
     * for wh
     */
    public BusLineRecycleAdapter(Context context, List<String> list) {
        this.context = context;
        mListWh = list;
        initHighList(mListWh.size());
    }

    private void initHighList(int size) {
        heightList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int height = new Random().nextInt(200) + 100;
            heightList.add(height);
        }
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
        ViewGroup.LayoutParams params = holder.tv_line_name.getLayoutParams();
        params.height = heightList.get(i);
        holder.tv_line_name.setLayoutParams(params);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusStopListActivity.class);
                switch (FitzApplication.getInstance().getDefaultCityCode()) {
                    case FitzApplication.city_code_SH:
                        Bundle b = new Bundle();
                        b.putParcelable(BusStopListActivity.EXTRAS_BBS_SH, mListSh.get(i));
                        intent.putExtras(b);
                        break;
                    case FitzApplication.city_code_WH:
                        intent.putExtra(BusStopListActivity.EXTRAS_BUSNAME_WH, mListWh.get(i));
                        break;
                    case FitzApplication.city_code_NJ:
                        break;
                    default:
                        return;
                }
                context.startActivity(intent);
            }
        });
    }

    private String getLineName(int i) {
        switch (FitzApplication.getInstance().getDefaultCityCode()) {
            case FitzApplication.city_code_SH:
                return mListSh.get(i).getLine_name();
            case FitzApplication.city_code_WH:
                return mListWh.get(i);
            case FitzApplication.city_code_NJ:
                break;
            default:
                return null;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        switch (FitzApplication.getInstance().getDefaultCityCode()) {
            case FitzApplication.city_code_SH:
                return mListSh.size();
            case FitzApplication.city_code_WH:
                return mListWh.size();
            case FitzApplication.city_code_NJ:
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
            tv_line_name = v.findViewById(R.id.busName);
            tv_end_stop = v.findViewById(R.id.end_stop);
        }
    }
}