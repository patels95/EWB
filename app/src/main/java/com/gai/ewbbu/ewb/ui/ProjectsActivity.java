package com.gai.ewbbu.ewb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gai.ewbbu.ewb.R;
import com.gai.ewbbu.ewb.adapters.ViewPagerAdapter;
import com.gai.ewbbu.ewb.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectsActivity extends ActionBarActivity implements ActionBar.TabListener,
    TaskFragment.OnFragmentInteractionListener {

    private static final String TAG = ProjectsActivity.class.getSimpleName();
    public static final String PROJECT_PARSE_ID = "PROJECT_PARSE_ID";

    private static String mProjectTitle;
    private static String mFirebaseKey;
    private ViewPagerAdapter mProjectPagerAdapter;
    private FirebaseAuth mFirebaseAuth;

    @BindView(R.id.tool_bar) Toolbar mToolbar;
    @BindView(R.id.projectTabs) TabLayout mProjectTabs;
    @BindView(R.id.projectPager) ViewPager mProjectPager;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();

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

        // Get the project's title from intent and set the action bar title
        Intent cardIntent = getIntent();
        mProjectTitle = cardIntent.getStringExtra(Constants.PROJECT_TITLE);
        mFirebaseKey = cardIntent.getStringExtra(Constants.FIREBASE_KEY);
        setTitle(mProjectTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (mFirebaseAuth.getCurrentUser() != null) {
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
            case R.id.action_logout:
                ParseUser.logOut();
                navigateToMain();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(ProjectsActivity.this, EditProjectsActivity.class);
                intent.putExtra(Constants.PARSE_ID, mFirebaseKey);
                startActivity(intent);
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

    public static String getFirebaseKey() {
        return mFirebaseKey;
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
