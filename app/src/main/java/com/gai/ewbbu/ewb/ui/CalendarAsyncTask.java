package com.gai.ewbbu.ewb.ui;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private static final String TAG = CalendarAsyncTask.class.getSimpleName();
    private String mPageToken;
    private Calendar mService;
    private String displayThisAccount; // ACCOUNT WHOSE CALENDAR YOU WANT TO DISPLAY
    private List<Event> mEventList = new ArrayList<>();
    private Boolean errorDetected = false;
    private Boolean isAccountSet = false;

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
        // From Google Developer documentation.
        // Pulls events from Calendar.Events.List (Not EventsList)

        do {
            Events events = null;
            try {
                Log.d(TAG, "account: " + displayThisAccount);
                Calendar.Events.List eventList = mService.events().list(displayThisAccount);
                Date date = new Date();
                com.google.api.client.util.DateTime dt = new DateTime(date);
                Log.d(TAG, date.toString());
                events = eventList
                        .setPageToken(mPageToken)
                        .setMaxResults(10)
                        .setOrderBy("startTime")
                        .setTimeMin(dt)
                        .setSingleEvents(true)
                        .execute();

            } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
                System.out.println("Error: GooglePlayServicesAvailabilityIOException");
                errorDetected = true;
                mFragment.showGooglePlayServicesAvailabilityErrorDialog(
                        availabilityException.getConnectionStatusCode());

            } catch (UserRecoverableAuthIOException userRecoverableException) {
                System.out.println("Error: UserRecoverableAuthIOException");
                errorDetected = true;
                if (mFragment.isAdded()) {
                    mFragment.startActivityForResult(
                            userRecoverableException.getIntent(),
                            CalendarFragment.REQUEST_AUTHORIZATION);
                }
            } catch (IOException e) {
                errorDetected = true;
                Log.d(TAG, "Error: IOException");
                Log.d(TAG, "error: " + e.toString());
                Log.d(TAG, "error: " + e.getCause().toString());
            }
            // If no errors are met, then account must have been set or was just set.
            isAccountSet = true;
            if (events != null) {
                mEventList = events.getItems();
                if (events.getNextPageToken() != null) {
                    // Theoretically, you should only have one page token. It should not ask for another one.
                    mPageToken = events.getNextPageToken();
//                    System.out.println("from AsyncTask " + mPageToken);
                }
            }
            else {
                System.out.println("CalendarAsyncTask.java - Account set. Please refresh the page.");
            }
        } while (mPageToken != null);
        mFragment.setEventList(mEventList);
        return mEventList;
    }

    @Override
    protected void onPostExecute(List<Event> resultEventList){
        // Take result from doInBackground and perform something with it.
        // In this case, return a valid mEventList.
        if (mEventList != null && response != null && errorDetected == false && mFragment.isAdded()) {
            System.out.println("CalendarAsyncTask.java - now on onPostExecute.");
            response.onTaskComplete(mEventList);
        }
    }
}
