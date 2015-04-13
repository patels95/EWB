package com.patels95.sanam.ewb.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {

    @Override
    public AnnouncementAdapter.AnnouncementViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(AnnouncementAdapter.AnnouncementViewHolder announcementViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AnnouncementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public AnnouncementViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
