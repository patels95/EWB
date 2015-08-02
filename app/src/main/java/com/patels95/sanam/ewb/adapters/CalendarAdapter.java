package com.patels95.sanam.ewb.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.media.MediaRecorder;
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

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;

import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.ui.CalendarDialog;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        TextView mEventTimeStartSubtext; // "Starts: "
        TextView mEventTimeEnd;
        TextView mEventTimeEndSubtext; // "Ends: "
        String mEventDescriptionString;

        public CalendarViewHolder(View v){
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mEventIcon = (ImageView) v.findViewById(R.id.eventIcon);
            mEventTitle = (TextView) v.findViewById(R.id.textEventTitle);
            mEventLocation = (TextView) v.findViewById(R.id.textEventLocation);
            mEventTimeStart = (TextView) v.findViewById(R.id.textEventStartTime);
            mEventTimeStartSubtext = (TextView) v.findViewById(R.id.textEventStart);
            mEventTimeEnd = (TextView) v.findViewById(R.id.textEventEndTime);
            mEventTimeEndSubtext = (TextView) v.findViewById(R.id.textEventEnd);

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
            setupTitle(holder, ev);
            setupLocation(holder, ev);
            setupTimeStart(holder, ev);
            setupTimeEnd(holder, ev);
            setupDescription(holder, ev);
        }
        }

    private void setupDescription(CalendarViewHolder holder, Event ev) {
        holder.mEventDescriptionString = ev.getDescription();
    }

    private void setupTitle(CalendarViewHolder holder, Event ev) {
        holder.mEventTitle.setText(ev.getSummary());
    }

    private void setupLocation(CalendarViewHolder holder, Event ev) {
        if (ev.getLocation() != null) {
            holder.mEventLocation.setText(ev.getLocation());
        } else {
            holder.mEventLocation.setVisibility(View.GONE);
        }
    }

    private void setupTimeStart(CalendarViewHolder holder, Event ev) {
        if (ev.getStart().getDateTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, hh:mm:ss");
            DateTime dt = ev.getStart().getDateTime();
            Date date = new Date(dt.getValue());
            String formattedDateTime = sdf.format(date);
            holder.mEventTimeStart.setText(formattedDateTime);
        } else if (ev.getStart().getDate() != null){ // This will assume the event is all day.
//            holder.mEventTimeStart.setVisibility(View.INVISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            com.google.api.client.util.DateTime dt = ev.getStart().getDate();
            Date date = new Date(dt.getValue());
            String formattedDate = sdf.format(date);
            holder.mEventTimeStart.setText(formattedDate + ", All Day");
//            holder.mEventTimeStartSubtext.setVisibility(View.INVISIBLE);
        }
    }

    private void setupTimeEnd(CalendarViewHolder holder, Event ev) {
        if (ev.getEnd().getDateTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, hh:mm:ss");
            DateTime dt = ev.getEnd().getDateTime();
            Date date = new Date(dt.getValue());
            String formattedDateTime = sdf.format(date);
            holder.mEventTimeEnd.setText(formattedDateTime);
        } else {
            holder.mEventTimeEnd.setVisibility(View.INVISIBLE);
            holder.mEventTimeEndSubtext.setVisibility(View.INVISIBLE);
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
