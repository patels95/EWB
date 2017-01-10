package com.gai.ewbbu.ewb.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.gai.ewbbu.ewb.R;
import com.gai.ewbbu.ewb.util.Constants;
import com.gai.ewbbu.ewb.model.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewTaskActivity extends ActionBarActivity {

    private static final String TAG = NewTaskActivity.class.getSimpleName();

    private static final java.lang.String DATE_PICKER_TAG = "DATE_PICKER";

    private static Calendar mDueDate = Calendar.getInstance();
    private String mFirebaseProjectKey;
    private DatabaseReference mDatabase;

    @BindView(R.id.tool_bar) Toolbar mToolbar;
    @BindView(R.id.new_task_title) EditText mTaskTitle;
    @BindView(R.id.new_task_description) EditText mTaskDescription;
    @BindView(R.id.saveTask) Button mSaveTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // use this to change up icon to check icon. use check to save new task
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_yourindicator);
        }

        Intent newTaskIntent = getIntent();
        mFirebaseProjectKey = newTaskIntent.getStringExtra(Constants.FIREBASE_KEY);

        mDatabase = FirebaseDatabase.getInstance().getReference();

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


    // save new task to firebase database
    public void saveTask() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        Task task = new Task(mTaskTitle.getText().toString(), mTaskDescription.getText().toString(),
                mFirebaseProjectKey, false, dateFormat.format(mDueDate.getTime()));


        DatabaseReference firebaseTasks = mDatabase.child(Constants.FIREBASE_TASKS_KEY).child(mFirebaseProjectKey);
        firebaseTasks.push().setValue(task);
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
