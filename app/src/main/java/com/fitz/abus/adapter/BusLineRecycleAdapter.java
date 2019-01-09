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
import android.widget.TextView;

import com.fitz.abus.R;
import com.fitz.abus.activity.BusStopListActivity;
import com.fitz.abus.bean.BusBaseSHBean;

import java.util.List;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.adapter
 * @ClassName: BusLineRecycleAdapter
 * @Author: Fitz
 * @CreateDate: 2018/12/23 20:02
 */
public class BusLineRecycleAdapter extends RecyclerView.Adapter<BusLineRecycleAdapter.FitzViewHolder> {

    private Context mcontext;
    private List<BusBaseSHBean> mList;

    public BusLineRecycleAdapter(Context context, List<BusBaseSHBean> list) {
        mcontext = context;
        mList = list;
    }

    @NonNull
    @Override
    public FitzViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.busline_recycleview_item, viewGroup, false);
        return new FitzViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FitzViewHolder holder, int i) {
        Log.d("BusLineRecycleAdapter", "onBindViewHolder");
        Log.d("BusLineRecycleAdapter", "count" + getItemCount());
        final int pos = i;
        holder.tv_line_name.setText(mList.get(i).getLine_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BusLineRecycleAdapter", "click" + "position:" + pos);
                Intent intent = new Intent(mcontext, BusStopListActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("busbaseSH", mList.get(pos));
                intent.putExtras(b);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
