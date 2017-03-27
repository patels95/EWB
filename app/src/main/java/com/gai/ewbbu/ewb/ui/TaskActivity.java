package com.gai.ewbbu.ewb.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.gai.ewbbu.ewb.R;
import com.gai.ewbbu.ewb.util.Constants;
import com.gai.ewbbu.ewb.model.Task;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TaskActivity.class.getSimpleName();

    private Task mTask;
    private static String mProjectTitle;
    private static String mFirebaseProjectKey;
    private String mFirebaseTaskKey;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;

    @BindView(R.id.tool_bar) Toolbar mToolbar;
    @BindView(R.id.taskTitle) TextView mTaskTitle;
    @BindView(R.id.taskDescription) TextView mTaskDescription;
    @BindView(R.id.completeTaskButton) FloatingActionButton mCompleteTaskButton;
    @BindView(R.id.taskDueDateText) TextView mDueDateText;

    // listener for confirming delete task
    private DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // delete the task
                    deleteTask();
                    Intent intent = new Intent(TaskActivity.this, ProjectsActivity.class);
                    intent.putExtra(Constants.PROJECT_TITLE, mProjectTitle);
                    intent.putExtra(Constants.FIREBASE_KEY, mFirebaseProjectKey);
                    startActivity(intent);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    // close dialog
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        mProjectTitle = intent.getStringExtra(Constants.PROJECT_TITLE);
        mFirebaseProjectKey = intent.getStringExtra(Constants.PROJECT_KEY);
        mFirebaseTaskKey = intent.getStringExtra(Constants.TASK_KEY);
        setTitle(mProjectTitle);

        if (mFirebaseAuth.getCurrentUser() == null) {
            mCompleteTaskButton.setVisibility(View.GONE);
        }

        mCompleteTaskButton.setOnClickListener(this);

        getTaskFromFirebase();
        checkIfTaskComplete();
//        setTaskInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (mFirebaseAuth.getCurrentUser() != null) {
            getMenuInflater().inflate(R.menu.menu_task, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                ParseUser.logOut();
                navigateToMain();
                break;
            case R.id.action_delete_task:
                // create confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure that you want to delete this task?");
                builder.setPositiveButton("Yes", mDialogListener);
                builder.setNegativeButton("Cancel", mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.completeTaskButton:
                completeTask();
        }
    }

    // start main activity after logout
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "You have been logged out.", Toast.LENGTH_LONG).show();
    }

    // get task info from firebase
    private void getTaskFromFirebase() {
        DatabaseReference taskRef = mDatabase.child(Constants.FIREBASE_TASKS_KEY)
                .child(mFirebaseProjectKey).child(mFirebaseTaskKey);
        taskRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTask = dataSnapshot.getValue(Task.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // show task info
                        setTaskInfo();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "get task:onCancelled", databaseError.toException());
            }
        });
    }

    private void checkIfTaskComplete() {
        Log.d(TAG, "check if task complete");
        DatabaseReference completeRef = mDatabase.child(Constants.FIREBASE_TASKS_KEY)
                .child(mFirebaseProjectKey).child(mFirebaseTaskKey).child(Constants.TASK_COMPLETE);
        completeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isComplete = (Boolean) dataSnapshot.getValue();
                toggleCompleteTaskButton(isComplete);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "get task complete:onCancelled", databaseError.toException());
            }
        });
    }

    private void toggleCompleteTaskButton(Boolean isComplete) {
        if (isComplete) {
            mCompleteTaskButton.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.complete_task)));
            mCompleteTaskButton.getDrawable().setColorFilter(ContextCompat.getColor(
                    this, R.color.white), PorterDuff.Mode.SRC_IN);
        }
        else {
            mCompleteTaskButton.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.white)));
            mCompleteTaskButton.getDrawable().setColorFilter(ContextCompat.getColor(
                    this, R.color.icon_grey), PorterDuff.Mode.SRC_IN);
        }
    }

    // set ui elements
    private void setTaskInfo() {
        mTaskTitle.setText(mTask.getTitle());
        mTaskDescription.setText(mTask.getDescription());
        mDueDateText.setText(mTask.getDateString());
    }

    public void completeTask() {
        final DatabaseReference completeRef = mDatabase.child(Constants.FIREBASE_TASKS_KEY)
                .child(mFirebaseProjectKey).child(mFirebaseTaskKey).child(Constants.TASK_COMPLETE);
        completeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isComplete = (Boolean) dataSnapshot.getValue();
                if (isComplete) {
                    // set task as incomplete
                    completeRef.setValue(false);
                }
                else {
                    // set task as complete
                    completeRef.setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // delete task from database
    private void deleteTask() {
        DatabaseReference taskRef = mDatabase.child(Constants.FIREBASE_TASKS_KEY)
                .child(mFirebaseProjectKey).child(mFirebaseTaskKey);
        taskRef.removeValue();
    }
}
