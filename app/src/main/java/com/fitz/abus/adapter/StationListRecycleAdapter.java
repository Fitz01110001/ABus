package com.fitz.abus.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fitz.abus.R;

public class StationListRecycleAdapter extends RecyclerView.Adapter<StationListRecycleAdapter.StationViewHolder> {


    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder stationViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class StationViewHolder extends RecyclerView.ViewHolder {
        TextView tv_line_name;
        TextView tv_end_stop;

        public StationViewHolder(View v) {
            super(v);
            tv_line_name = v.findViewById(R.id.line_name);
            tv_end_stop = v.findViewById(R.id.end_stop);
        }
    }
}
