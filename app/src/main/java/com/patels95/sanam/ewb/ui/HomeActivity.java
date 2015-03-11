package com.patels95.sanam.ewb.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.patels95.sanam.ewb.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class HomeActivity extends ActionBarActivity {

    private ListView mAnnouncementList;
    @InjectView(R.id.projectsButton) Button mProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);

        mAnnouncementList = (ListView) findViewById(R.id.announcementList);

//        String[] listValues = new String[] {
//                "Announcement 1",
//                "Announcement 2",
//                "Announcement 3",
//                "Announcement 4"
//        };

        mProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProjects();
            }
        });

    }

    private void startProjects() {
        Intent intent = new Intent(this, ProjectsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
