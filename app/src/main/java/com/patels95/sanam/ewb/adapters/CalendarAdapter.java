package com.patels95.sanam.ewb.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.patels95.sanam.ewb.R;

import java.util.List;

/**
 * Created by Daniel on 7/22/2015.
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    // Constructor variable
    private List<Event> mDataset;

    public static class CalendarViewHolder extends RecyclerView.ViewHolder{
        // Event variables
        CardView mCardView;
        ImageView mEventIcon;
        TextView mEventTitle;
        TextView mEventLocation;
        TextView mEventTimeStart;
        TextView mEventTimeEnd;
        TextView mEventDescription;

        CalendarViewHolder(View v){
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mEventIcon = (ImageView) v.findViewById(R.id.eventIcon);
            mEventTitle = (TextView) v.findViewById(R.id.textEventTitle);
            mEventLocation = (TextView) v.findViewById(R.id.textEventLocation);
            mEventTimeStart = (TextView) v.findViewById(R.id.textEventStartTime);
            mEventTimeEnd = (TextView) v.findViewById(R.id.textEventEndTime);
        }
    }

    public CalendarAdapter(List<Event> list){
        this.mDataset = list;
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
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
        // Attach variables to appropriate locations here.
        holder.mEventTitle.setText("TESTING TITLE TESTING TITLE");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        // return mDataset.size()
        if (mDataset == null){
            return 1; // Debugging. Should have at least one card view.
        }
        else {
            return mDataset.size();
        }
    }
}
