package com.patels95.sanam.ewb.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.model.Project;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProjectsActivity extends ActionBarActivity {

    @InjectView(R.id.leftArrow) ImageView mLeftArrow;
    @InjectView(R.id.projectTitle) TextView mProjectTitle;
    @InjectView(R.id.rightArrow) ImageView mRightArrow;

    ArrayList<Project> Projects = new ArrayList<Project>();
    private int currentProjectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        ButterKnife.inject(this);

        initiateProjectData();

        mLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftArrowClick();
            }
        });

        mRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightArrowClick();
            }
        });

    }

    private void rightArrowClick() {
        if(currentProjectIndex ==  (Projects.size() - 1)){
            currentProjectIndex = 0;
        }
        else{
            currentProjectIndex += 1;
        }
        Project project = Projects.get(currentProjectIndex);
        mProjectTitle.setText(project.getTitle());
    }

    private void leftArrowClick() {
        if(currentProjectIndex == 0){
            currentProjectIndex = Projects.size() - 1;
        }
        else{
            currentProjectIndex -= 1;
        }
        Project project = Projects.get(currentProjectIndex);
        mProjectTitle.setText(project.getTitle());
    }

    //add default project data to projects ArrayList
    private void initiateProjectData() {
        Projects.add(new Project("Project0", "Description0", 0));
        Projects.add(new Project("Project1", "Description1", 1));
        Projects.add(new Project("Project2", "Description2", 2));
        Projects.add(new Project("Project3", "Description3", 3));
        mProjectTitle.setText(Projects.get(0).getTitle());
        currentProjectIndex = 0;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_projects, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
