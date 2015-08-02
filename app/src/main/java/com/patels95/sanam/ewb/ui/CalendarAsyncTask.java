package com.patels95.sanam.ewb.ui;

import android.os.AsyncTask;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Daniel on 7/17/2015.
 * This AsyncTask gets API data from Google Calendar on a separate thread.
 */
public class CalendarAsyncTask extends AsyncTask<Void, Void, List<Event>> {
    // INTERFACE HERE, interface lets us move Async result to the actual fragment.
    public CalendarAsyncInterface response = null;
    // FRAGMENT variable
    private CalendarFragment mFragment;
    // Member variables
    private static final String TAG = CalendarFragment.class.getSimpleName();
    private String mPageToken;
    private Calendar mService;
    private String displayThisAccount; // ACCOUNT WHOSE CALENDAR YOU WANT TO DISPLAY
    private List<Event> mEventList = new ArrayList<>();
    private Integer mCounter = 0;

    CalendarAsyncTask(CalendarFragment frag, CalendarAsyncInterface listener, String pageToken, Calendar calendar, String account){
        mFragment = frag;
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
                Calendar.Events.List eventList = mService.events().list(displayThisAccount);
                Date date = new Date();
                com.google.api.client.util.DateTime dt = new DateTime(date);
                eventList.setTimeMin(dt);
                events = eventList
                        .setPageToken(mPageToken)
                        .setMaxResults(10)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();
            } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
                mFragment.showGooglePlayServicesAvailabilityErrorDialog(
                        availabilityException.getConnectionStatusCode());
            } catch (UserRecoverableAuthIOException userRecoverableException) {
                mFragment.startActivityForResult(
                        userRecoverableException.getIntent(),
                        CalendarFragment.REQUEST_AUTHORIZATION);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mCounter == 0 && events != null){
                mEventList = events.getItems();
            }
            mPageToken = events.getNextPageToken();
            mCounter += 1;
        } while (mPageToken != null);
        mFragment.setEventList(mEventList);
        return mEventList;
    }

    @Override
    protected void onPostExecute(List<Event> resultEventList){
        // Take result from doInBackground and perform something with it.
        // In this case, return a valid mEventList.
        if (mEventList != null && response != null) {
            response.onTaskComplete(mEventList);
        }
    }
}
