package com.patels95.sanam.ewb.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.adapters.AnnouncementAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class HomeOldActivity extends ActionBarActivity {

    private boolean mIsMember;

    @InjectView(R.id.projectsImageView) ImageView mProjectsImage;
    @InjectView(R.id.announcementList) RecyclerView mAnnouncementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_old);
        ButterKnife.inject(this);
        setUserType();

        mProjectsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startProjects();
            }
        });

        // Announcement RecyclerView
        AnnouncementAdapter adapter = new AnnouncementAdapter(); // insert parameters for constructor
        mAnnouncementList.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager =  new LinearLayoutManager(this);
        mAnnouncementList.setLayoutManager(layoutManager);
        mAnnouncementList.setHasFixedSize(true);

    }

    // set the mIsMember variable for member or guest
    private void setUserType() {
        Intent intent = getIntent();
        mIsMember = intent.getBooleanExtra(MainActivity.IS_MEMBER, false);
    }

    // set the available actions in the action bar based on the type of user


//    private void startProjects() {
//        Intent intent = new Intent(this, ProjectsActivity.class);
//        startActivity(intent);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        MenuItem item = menu.findItem(R.id.action_edit);
        if(!mIsMember){
            item.setVisible(false);
        }
        this.invalidateOptionsMenu();

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
