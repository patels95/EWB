package com.patels95.sanam.ewb.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.ui.CalendarDialog;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 7/22/2015.
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    // Constructor variable
    private Context mContext;
    private List<Event> mDataset = new ArrayList<>();

    public CalendarAdapter(Context context, List<Event> list){
        this.mContext = context;
        this.mDataset = list;
    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Event variables
        CardView mCardView;
        ImageView mEventIcon;
        TextView mEventTitle;
        TextView mEventLocation;
        TextView mEventTimeStart;
        TextView mEventTimeEnd;
        String mEventDescriptionString;

        public CalendarViewHolder(View v){
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mEventIcon = (ImageView) v.findViewById(R.id.eventIcon);
            mEventTitle = (TextView) v.findViewById(R.id.textEventTitle);
            mEventLocation = (TextView) v.findViewById(R.id.textEventLocation);
            mEventTimeStart = (TextView) v.findViewById(R.id.textEventStartTime);
            mEventTimeEnd = (TextView) v.findViewById(R.id.textEventEndTime);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CalendarDialog eventDialog = new CalendarDialog();
            eventDialog = eventDialog.newInstance(mEventTitle.getText().toString(),
                                    mEventDescriptionString);
            eventDialog.show(((Activity) mContext).getFragmentManager(), "null");
        }
    }
    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Set the new view here.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_calendarevent, parent, false);
        CalendarViewHolder cvh = new CalendarViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, int position) {
        if (mDataset != null) {
            Event ev = mDataset.get(position);
            holder.mEventTitle.setText(ev.getSummary());
            holder.mEventLocation.setText(ev.getLocation());
            holder.mEventTimeStart.setText(ev.getStart().toString());
            holder.mEventTimeEnd.setText(ev.getEnd().toString());
            holder.mEventDescriptionString = ev.getDescription();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addItemToDataset(Event event){
        if (mDataset == null){
            mDataset = new ArrayList<>();
        }
        this.mDataset.add(event);
    }
    public boolean isDatasetNull(){
        if (mDataset == null){
            return true;
        }
        else return false;
    }

    @Override
    public int getItemCount() {
        if (mDataset == null){
            return 0; // Debugging. Should have at least one card view.
        }
        else {
            return mDataset.size();
        }
    }
}
