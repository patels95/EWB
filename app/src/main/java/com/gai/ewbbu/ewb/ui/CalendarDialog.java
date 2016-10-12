package com.gai.ewbbu.ewb.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.services.calendar.model.EventAttachment;
import com.gai.ewbbu.ewb.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Daniel on 7/31/2015.
 */
public class CalendarDialog extends android.app.DialogFragment {
    private String mEventTitle;
    private String mEventDescription;
    private CalendarSerializable mCalendarSerializable;
    private List<EventAttachment> mEventAttachments;
    private List<String> mEventAttachmentsUrl;

    public static CalendarDialog newInstance (String title, String desc, Serializable attach){
        CalendarDialog cd = new CalendarDialog();
        Bundle args = new Bundle();
        args.putString("EVENT_TITLE", title);
        args.putString("EVENT_DESC", desc);
        args.putSerializable("EVENT_ATTACH", attach);
        cd.setArguments(args);
        return cd;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mEventTitle = getArguments().getString("EVENT_TITLE");
        mEventDescription = getArguments().getString("EVENT_DESC");
        mCalendarSerializable = (CalendarSerializable) getArguments().getSerializable("EVENT_ATTACH");
        mEventAttachments = mCalendarSerializable.getEventAttachments();

        Context context = getActivity();
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_calendar);
        TextView title = (TextView) dialog.findViewById(R.id.textTitle);
        TextView body = (TextView) dialog.findViewById(R.id.textBody);
        LinearLayout attachLayout = (LinearLayout) dialog.findViewById(R.id.layoutAttachments);
        Button button = (Button) dialog.findViewById(R.id.buttonOK);

        title.setText(mEventTitle);
        body.setText (mEventDescription);
        attachLayout.setVisibility(View.GONE);
        // Attachment feature for the dialog.
        // If an attachment is available, it will appear at the bottom under "Attachments"
        // An attachment is tied to the Google Drive of EWB's account.
        // An attachment MUST be set as public in Google Drive, otherwise it cannot be accessed by anyone.
        // The user will be prompted to access EWB's google drive.
        if (mEventAttachments != null){
            attachLayout.setVisibility(View.VISIBLE);
            for (EventAttachment ea :  mEventAttachments){
                TextView urlView = new TextView(context);
                urlView.setText(ea.getTitle());
                final String stringUrl = ea.getFileUrl();
//                final String stringUrl = ea.getFileUrl
                urlView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(stringUrl);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
                        dismiss();
                        startActivity(launchBrowser);
                    }
                });
                attachLayout.addView(urlView);
            }
        }
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return dialog;
    }

    private void extractUrls() {
        if (mEventAttachments != null){
            for (EventAttachment ea : mEventAttachments){
                String url = ea.getTitle() + "\n";
                mEventAttachmentsUrl.add(url);
            }
        }
    }
}
