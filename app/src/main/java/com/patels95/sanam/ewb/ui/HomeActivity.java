package com.patels95.sanam.ewb.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.patels95.sanam.ewb.R;


public class HomeActivity extends ActionBarActivity {

    private ListView mAnnouncementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAnnouncementList = (ListView) findViewById(R.id.announcementList);

//        String[] listValues = new String[] {
//                "Announcement 1",
//                "Announcement 2",
//                "Announcement 3",
//                "Announcement 4"
//        };

    }

}
