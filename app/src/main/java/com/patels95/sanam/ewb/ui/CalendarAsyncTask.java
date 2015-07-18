package com.patels95.sanam.ewb.ui;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.List;

/**
 * Created by Daniel on 7/17/2015.
 */
public class CalendarAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = CalendarFragment.class.getSimpleName();
    private String mPageToken;
    private Calendar mService;
    private String displayThisAccount;

    CalendarAsyncTask(String pageToken, Calendar calendar){
        mPageToken = pageToken;
        mService = calendar;
    }
    @Override
    protected String doInBackground(Void... params) {
        do {
            Events events = null;
            try {
                events = mService.events().list("metlifeyet@gmail.com").setPageToken(mPageToken).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Event> items = events.getItems();
            for (Event event : items) {
                System.out.println(event.getSummary());
            }
            mPageToken = events.getNextPageToken();
        } while (mPageToken != null);
        return mPageToken;
    }

    public String getPageToken() {return mPageToken;}
}
