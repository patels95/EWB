package com.patels95.sanam.ewb.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.parse.ParseObject;
import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.model.ParseConstants;

import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewTaskActivity extends ActionBarActivity {

    private static final String TAG = NewTaskActivity.class.getSimpleName();

    private static final java.lang.String DATE_PICKER_TAG = "DATE_PICKER";
    @InjectView(R.id.new_task_title) EditText mTaskTitle;
    @InjectView(R.id.new_task_description) EditText mTaskDescription;
    @InjectView(R.id.saveTask) Button mSaveTaskButton;

    private static Calendar mDueDate = Calendar.getInstance();

    private String mProjectParseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.inject(this);

        Intent newTaskIntent = getIntent();
        mProjectParseId = newTaskIntent.getStringExtra(ProjectsActivity.PROJECT_PARSE_ID);

        mSaveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    // called when due date button is clicked
    public void showDatePicker(View view) {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
    }


    // called when save button is clicked
    public void saveTask() {
        ParseObject task = new ParseObject(ParseConstants.TASK_CLASS);
        task.put(ParseConstants.TASK_TITLE, mTaskTitle.getText().toString());
        task.put(ParseConstants.TASK_DESCRIPTION, mTaskDescription.getText().toString());
        task.put(ParseConstants.TASK_COMPLETE, false);
        task.put(ParseConstants.TASK_DUE_DATE, mDueDate.getTime());
        task.put(ParseConstants.TASK_PROJECT_ID, mProjectParseId);
        task.saveInBackground();
        finish();
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // try this again
            mDueDate.set(year, monthOfYear, dayOfMonth);
            // use a listener to set member variables
            Log.d(TAG, "date: " + monthOfYear + "/" + dayOfMonth + "/" + year);
        }
    }
}
