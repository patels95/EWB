package com.patels95.sanam.ewb.ui;

import android.Manifest;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.adapters.CalendarAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = CalendarFragment.class.getSimpleName();

    // This email will be used for the calendar.
    private String USETHISEMAIL = "ewbbu.webmaster@gmail.com";

    // Fragment interface variables.
    private LinearLayout mFragmentLayout;
    private ProgressBar mProgressBar;
    private TextView mLoadingText;
    private Button mRefreshButton;

    //AsyncTask tools / interface.
    private CalendarAsyncInterface mInterface = new CalendarAsyncInterface() {
        @Override
        public void onTaskComplete(List<Event> result) {
            //this you will received result fired from async class of onPostExecute(result) method.
            System.out.println("CalendarFragment - mInterface onTaskComplete complete.");
            if (result != null){
                mEventList = result;
                extractEventListData();
            }
            else{
                mEventList = null;
            }
        }
    };

    // No idea what does this.
    private int mSectionNumber;

    // Recycler View.
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private CalendarAdapter mCalendarAdapter;

    // Task related variables.
    private List<Event> mEventList;
    private OnFragmentInteractionListener mListener;

    /**
     * A Google Calendar API service object used to access the API.
     * Note: Do not confuse this class with API library's model classes, which
     * represent specific data structures.
     */
    com.google.api.services.calendar.Calendar mService;

    GoogleAccountCredential credential;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    public static CalendarFragment newInstance(int sectionNumber) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendarFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }
//        // Initialize credentials and service object.
        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        String accountName = settings.getString(PREF_ACCOUNT_NAME, "");
        Log.d(TAG, "user email: " + accountName);

        credential = GoogleAccountCredential.usingOAuth2(
                getActivity().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(accountName);

        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Engineers Without Borders BU")
                .build();

        refreshResults();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Callever every time the fragment is utilized.
        // Adapter functions.
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mFragmentLayout = (LinearLayout) view.findViewById(R.id.fragmentLayout);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mLoadingText = (TextView) view.findViewById(R.id.textFragment);
        mRefreshButton = (Button) view.findViewById(R.id.refreshButton);
        // If app successfully retrieves data from API
        mProgressBar.setVisibility(View.GONE);
        mFragmentLayout.setBackgroundColor(Color.WHITE);
        mLoadingText.setVisibility(View.GONE);
        // Set up RecyclerView / CardView for event catalogue.
        mRecyclerView = (RecyclerView) view.findViewById(R.id.calendarRecyclerView);
        mRecyclerView.setVisibility(View.VISIBLE);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true); // Experimental. Remove if recyclerView is not fixed.
        mCalendarAdapter = new CalendarAdapter(getActivity(), mEventList);
        mRecyclerView.setAdapter(mCalendarAdapter);
        // Set up refresh button. Refresh button visible if no events are found via

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshResults();
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void startAsyncCalendar() {
        Calendar service = new Calendar.Builder(transport, jsonFactory, credential)
                .setApplicationName("EWB").build();
        String pageToken = null;
        CalendarAsyncTask task = new CalendarAsyncTask(this, mInterface, pageToken, service, USETHISEMAIL);
        task.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setEventList(List<Event> eventList) {
        mEventList = eventList;
    }

    /**
 * Once the asynctask is complete, then mEventList should be initiated with an EventList.
 * This method successfully extracts the required data and loads it into the CalendarAdapter.
 * If the EventList is null, then the method will return a toast message warning the user.
 */
    private void extractEventListData() {
        mCalendarAdapter.clearList(); // Reset list.
        if (mEventList.size() != 0){
            for (Event event : mEventList) {
                System.out.println("extractEventListData() - event extracted.");
                mCalendarAdapter.addItemToDataset(event);
            }
            mLoadingText.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mCalendarAdapter.notifyDataSetChanged();
        }
        else {
            System.out.println("extractEventListData() - mEventList was null.");
            mLoadingText.setText("No events were found.");
            mLoadingText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "No events found! Please refresh this page.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /** GOOGLE CALENDAR METHODS **/
    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */

    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != getActivity().RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == getActivity().RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    System.out.println("This is accountName: " + accountName);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        startAsyncCalendar();
                    }
                } else if (resultCode == getActivity().RESULT_CANCELED) { // 0
//                    mStatusText.setText("Account unspecified.");
                    Toast.makeText(getActivity(), "Account unspecified.", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != getActivity().RESULT_OK) {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_GET_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseAccount();
                }
                else {
                    Toast.makeText(getActivity(), "Permission must be granted to view EWB calendar.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * Attempt to get a set of data from the Google Calendar API to display. If the
     * email address isn't known yet, then call chooseAccount() method so the
     * user can pick an account.
     */
    private void refreshResults() {
        Log.d(TAG, "credential: " + credential.toString());
        if (credential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline() && credential.getSelectedAccountName() != null) {
                startAsyncCalendar();
            } else if (isDeviceOnline() == false){
                Toast.makeText(getActivity(), "No network connection is available.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(), "Credential account is null.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
    private void chooseAccount() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.GET_ACCOUNTS}, REQUEST_PERMISSION_GET_ACCOUNTS);
        }
        else {
            startActivityForResult(
                    credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
        }
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date. Will
     * launch an error dialog for the user to update Google Play Services if
     * possible.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,
                        getActivity(),
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }
    

}
