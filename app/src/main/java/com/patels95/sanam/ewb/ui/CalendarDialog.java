package com.patels95.sanam.ewb.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.patels95.sanam.ewb.R;

/**
 * Created by Daniel on 7/31/2015.
 */
public class CalendarDialog extends android.app.DialogFragment {
    private String mEventTitle;
    private String mEventDescription;

    public static CalendarDialog newInstance (String title, String desc){
        CalendarDialog cd = new CalendarDialog();
        Bundle args = new Bundle();
        args.putString("mEventTitle", title);
        args.putString("mEventDescription", desc);
        cd.setArguments(args);
        return cd;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mEventTitle = getArguments().getString("mEventTitle");
        mEventDescription = getArguments().getString("mEventDescription");
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(mEventTitle);
        builder.setMessage(mEventDescription);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
