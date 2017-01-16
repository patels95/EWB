package com.gai.ewbbu.ewb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.gai.ewbbu.ewb.R;
import com.gai.ewbbu.ewb.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProjectsActivity extends ActionBarActivity {

    private String mParseId;

    @BindView(R.id.tool_bar) Toolbar mToolbar;
    @BindView(R.id.editName) EditText mEditName;
    @BindView(R.id.editDescription) EditText mEditDescription;
    @BindView(R.id.editButton) Button mEditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_projects);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // get parse objectid from projects activity
        Intent projectIntent = getIntent();
        mParseId = projectIntent.getStringExtra(Constants.PARSE_ID);

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateParseProject();
                Intent intent = new Intent(EditProjectsActivity.this, ProjectsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateParseProject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");

        // retrieve the object by id
        query.getInBackground(mParseId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject project, ParseException e) {
                if (e == null) {
                    String title = mEditName.getText().toString();
                    String description = mEditDescription.getText().toString();

                    // update project details if field is not empty
                    if (!title.isEmpty()) {
                        project.put(Constants.PROJECT_TITLE, mEditName.getText().toString());
                    }
                    if (!description.isEmpty()) {
                        project.put(Constants.PROJECT_DESCRIPTION, mEditDescription.getText().toString());
                    }
                    project.saveInBackground();
                    Toast.makeText(EditProjectsActivity.this, "Changes have been saved.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(EditProjectsActivity.this, "Error. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
