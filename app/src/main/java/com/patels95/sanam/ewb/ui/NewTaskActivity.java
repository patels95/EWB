package com.patels95.sanam.ewb.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;

import com.patels95.sanam.ewb.R;

import butterknife.InjectView;

public class NewTaskActivity extends ActionBarActivity {

    @InjectView(R.id.new_task_title) EditText mTaskTitle;
    @InjectView(R.id.new_task_description) EditText mTaskDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
    }

}
