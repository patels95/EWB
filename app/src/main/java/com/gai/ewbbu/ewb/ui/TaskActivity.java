package com.gai.ewbbu.ewb.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.gai.ewbbu.ewb.R;
import com.gai.ewbbu.ewb.model.ParseConstants;
import com.gai.ewbbu.ewb.model.Task;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskActivity extends AppCompatActivity {

    private static final String TAG = TaskActivity.class.getSimpleName();

    @BindView(R.id.tool_bar) Toolbar mToolbar;
    @BindView(R.id.taskTitle) TextView mTaskTitle;
    @BindView(R.id.taskDescription) TextView mTaskDescription;
    @BindView(R.id.completeTask) Button mCompleteTask;

    private String mTaskId;
    private Task mTask;
    private static String mProjectTitle;
    private static String mProjectParseId;
    private FirebaseAuth mFirebaseAuth;


    private DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // delete the task
                    deleteTask();
                    Intent intent = new Intent(TaskActivity.this, ProjectsActivity.class);
                    intent.putExtra(ParseConstants.PROJECT_TITLE, mProjectTitle);
                    intent.putExtra(ParseConstants.PARSE_ID, mProjectParseId);
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

        Intent intent = getIntent();
        mProjectTitle = intent.getStringExtra(ParseConstants.PROJECT_TITLE);
        mProjectParseId = intent.getStringExtra(ParseConstants.PARSE_ID);
        mTaskId = intent.getStringExtra(ParseConstants.TASK_ID);
        setTitle(mProjectTitle);

        if (mFirebaseAuth.getCurrentUser() == null) {
            mCompleteTask.setVisibility(View.GONE);
        }

        mTask = getTaskFromParse(mTaskId);
        setTaskInfo();
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

    // start main activity after logout
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "You have been logged out.", Toast.LENGTH_LONG).show();
    }

    // get Task from parse using task id
    private Task getTaskFromParse(String taskId) {
        final Task task = new Task();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.TASK_CLASS);
        try {
            ParseObject object = query.get(taskId);
            task.setTitle(object.getString(ParseConstants.TASK_TITLE));
            task.setDescription(object.getString(ParseConstants.TASK_DESCRIPTION));
            task.setTaskId(object.getString(ParseConstants.TASK_ID));
            task.setProjectId(object.getString(ParseConstants.TASK_PROJECT_ID));
            task.setComplete(object.getBoolean(ParseConstants.TASK_COMPLETE));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(object.getDate(ParseConstants.TASK_DUE_DATE));
            task.setDueDate(calendar);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return task;
    }

    // set ui elements
    private void setTaskInfo() {
        mTaskTitle.setText(mTask.getTitle());
        mTaskDescription.setText(mTask.getDescription());
    }

    public void completeTask(View view) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.TASK_CLASS);
        query.getInBackground(mTaskId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject task, ParseException e) {
                task.put(ParseConstants.TASK_COMPLETE, true);
                task.saveInBackground();
                Toast.makeText(TaskActivity.this, "This task has been marked as complete", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteTask() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.TASK_CLASS);
        query.getInBackground(mTaskId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject task, ParseException e) {
                task.deleteInBackground();
                Toast.makeText(TaskActivity.this, "This task has been deleted", Toast.LENGTH_LONG).show();
            }
        });
    }
}
