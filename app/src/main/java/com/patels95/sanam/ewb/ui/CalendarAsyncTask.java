package com.patels95.sanam.ewb.ui;

import android.os.AsyncTask;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 7/17/2015.
 * This AsyncTask gets API data from Google Calendar on a separate thread.
 */
public class CalendarAsyncTask extends AsyncTask<Void, Void, List<Event>> {
    // INTERFACE HERE, interface lets us move Async result to the actual fragment.
    public CalendarAsyncInterface response = null;

    // Member variables
    private static final String TAG = CalendarFragment.class.getSimpleName();
    private String mPageToken;
    private Calendar mService;
    private String displayThisAccount;
    private List<Event> mEventList = new ArrayList<>();
    private Integer mCounter = 0;

    CalendarAsyncTask(CalendarAsyncInterface listener, String pageToken, Calendar calendar, String account){
        response = listener;
        mPageToken = pageToken;
        mService = calendar;
        displayThisAccount = account;
    }

    CalendarAsyncTask(){ // Default Constructor.
    }
    @Override
    protected List<Event> doInBackground(Void... params) {
        do {
            Events events = null;
            try {
                events = mService.events().list(displayThisAccount).setPageToken(mPageToken).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Event> items = events.getItems();
            if (mCounter == 0 && events != null){
//                System.out.println("mCounter raised.");
                mEventList = events.getItems();
                mCounter = 1;
            }

            for (Event event : items) {
                // Print all event summaries in Logcat.
//                System.out.println(event.getSummary());
//                System.out.println("Event name: " + event.getSummary());
//                System.out.println("Description: " + event.getDescription());
//                System.out.println("Location:" + event.getLocation());
            }
            mPageToken = events.getNextPageToken();
        } while (mPageToken != null);
        return mEventList;
    }

    protected void onPostExecute(List<Event> resultEventList){
        // Take result from doInBackground and perform something with it.
        // In this case, return a valid mEventList.
        if (mEventList != null && response != null) {
//            Event event = mEventList.get(0);
//            System.out.println("Event name: " + event.getSummary());
            response.onTaskComplete(mEventList);
        }
    }
}
