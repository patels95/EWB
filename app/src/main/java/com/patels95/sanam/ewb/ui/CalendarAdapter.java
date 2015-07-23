package com.patels95.sanam.ewb.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patels95.sanam.ewb.R;

import java.util.ArrayList;

/**
 * Created by Daniel on 7/22/2015.
 */
public class CalendarAdapter extends RecyclerView.Adapter {
    private ArrayList<String> mDataset;

    public CalendarAdapter(ArrayList<String> mDataset) {
        this.mDataset = mDataset;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
