package com.wind.fitz.slideviewdemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wind.fitz.slideviewdemo.R;

import java.util.List;

/**
 * @ProjectName: ABus
 * @Package: com.wind.fitz.slideviewdemo.adapter
 * @Author: Fitz
 * @CreateDate: 2019/1/10
 */
public class FragmentListAdapter extends RecyclerView.Adapter<FragmentListAdapter.FitzViewHolder> {

    private List<Integer> list;
    private Context context;


    public FragmentListAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FitzViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item, viewGroup, false);
        return new FitzViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FitzViewHolder fitzViewHolder, int i) {
        fitzViewHolder.setLineName(i+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FitzViewHolder extends RecyclerView.ViewHolder {

        private TextView lineName;

        public FitzViewHolder(@NonNull View itemView) {
            super(itemView);
            lineName = itemView.findViewById(R.id.tv_line_name);
        }

        public void setLineName(String s){
            lineName.setText(s);
        }
    }


}
