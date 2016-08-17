package com.patels95.sanam.ewb.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;
import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.adapters.ProjectAdapter;
import com.patels95.sanam.ewb.adapters.SectionsPagerAdapter;
import com.patels95.sanam.ewb.adapters.ViewPagerAdapter;
import com.patels95.sanam.ewb.model.ParseConstants;
import com.patels95.sanam.ewb.model.Project;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProjectsActivity extends ActionBarActivity implements ActionBar.TabListener,
    TaskFragment.OnFragmentInteractionListener {

    private static final String TAG = ProjectsActivity.class.getSimpleName();
    public static final String PROJECT_PARSE_ID = "PROJECT_PARSE_ID";

    private static String mProjectTitle;
    private static String mParseId;
    private ViewPagerAdapter mProjectPagerAdapter;

    @InjectView(R.id.tool_bar) Toolbar mToolbar;
    @InjectView(R.id.projectTabs) TabLayout mProjectTabs;
    @InjectView(R.id.projectPager) ViewPager mProjectPager;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set up the action bar.
//        final ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.accent)));

        mProjectPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mProjectPager.setAdapter(mProjectPagerAdapter);


        TabLayout.Tab tasks = mProjectTabs.newTab();
        TabLayout.Tab resources = mProjectTabs.newTab();

        tasks.setText("Tasks");
        resources.setText("Resources");

        mProjectTabs.addTab(tasks, 0);
        mProjectTabs.addTab(resources, 1);

        mProjectTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mProjectPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mProjectPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mProjectTabs));


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
//        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                actionBar.setSelectedNavigationItem(position);
//            }
//        });

        // For each of the sections in the app, add a tab to the action bar.
//        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
//            // Create a tab with text corresponding to the page title defined by
//            // the adapter. Also specify this Activity object, which implements
//            // the TabListener interface, as the callback (listener) for when
//            // this tab is selected.
//            actionBar.addTab(
//                    actionBar.newTab()
//                            .setText(mSectionsPagerAdapter.getPageTitle(i))
//                            .setTabListener(this));
//        }

        // Get the project's title from intent and set the action bar title
        Intent cardIntent = getIntent();
        mProjectTitle = cardIntent.getStringExtra(ParseConstants.PROJECT_TITLE);
        mParseId = cardIntent.getStringExtra(ParseConstants.PARSE_ID);
        setTitle(mProjectTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (ParseUser.getCurrentUser() != null) {
            getMenuInflater().inflate(R.menu.menu_projects, menu);
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
            case R.id.action_edit:
                Intent intent = new Intent(ProjectsActivity.this, EditProjectsActivity.class);
                intent.putExtra(ParseConstants.PARSE_ID, mParseId);
                startActivity(intent);
                break;
            case R.id.action_new:
                Intent newTaskIntent = new Intent(ProjectsActivity.this, NewTaskActivity.class);
                newTaskIntent.putExtra(PROJECT_PARSE_ID, mParseId);
                startActivity(newTaskIntent);
                break;
            case android.R.id.home:
                onBackPressed();
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

    public static String getProjectTitle() {
        return mProjectTitle;
    }

    public static String getParseProjectId() {
        return mParseId;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
