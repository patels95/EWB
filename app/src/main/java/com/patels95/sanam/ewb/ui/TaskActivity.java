package com.patels95.sanam.ewb.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.model.ParseConstants;
import com.patels95.sanam.ewb.model.Task;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TaskActivity extends AppCompatActivity {

    @InjectView(R.id.taskTitle) TextView mTaskTitle;
    @InjectView(R.id.taskDescription) TextView mTaskDescription;

    private String mTaskId;
    private Task mTask;

    private DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // delete the task
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
        ButterKnife.inject(this);

        Intent intent = getIntent();
        String projectTitle = intent.getStringExtra(ParseConstants.PROJECT_TITLE);
        mTaskId = intent.getStringExtra(ParseConstants.TASK_ID);
        setTitle(projectTitle);

        mTask = getTaskFromParse(mTaskId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (ParseUser.getCurrentUser() != null) {
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

    private Task getTaskFromParse(String taskId) {
        final Task task = new Task();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.TASK_CLASS);
        query.getInBackground(taskId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                task.setTitle(parseObject.getString(ParseConstants.TASK_TITLE));
                task.setDescription(parseObject.getString(ParseConstants.TASK_DESCRIPTION));
                task.setTaskId(parseObject.getString(ParseConstants.TASK_ID));
                task.setProjectId(parseObject.getString(ParseConstants.TASK_PROJECT_ID));
                task.setComplete(parseObject.getBoolean(ParseConstants.TASK_COMPLETE));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parseObject.getDate(ParseConstants.TASK_DUE_DATE));
                task.setDueDate(calendar);
            }
        });
        return task;
    }

}
