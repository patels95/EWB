package com.patels95.sanam.ewb.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.ParseUser;
import com.patels95.sanam.ewb.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
        CalendarFragment.OnFragmentInteractionListener,
        ProjectsFragment.OnFragmentInteractionListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private FragmentManager mFragmentManager = getSupportFragmentManager();

    @InjectView(R.id.tool_bar) Toolbar mToolbar;
    @InjectView(R.id.navigation_view) NavigationView mNavigationView;
    @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;

    private NavigationView.OnNavigationItemSelectedListener mNavigationItemSelectedListener =
            new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if (item.isChecked()) {
                item.setChecked(false);
            }
            else {
                item.setChecked(true);
            }

            mDrawerLayout.closeDrawers();

            switch (item.getItemId()) {
                case R.id.twitter:
                    mTitle = "Twitter";
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, HomeFragment.newInstance(0))
                            .commit();
                    return true;
                case R.id.calendar:
                    mTitle = "Calendar";
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, CalendarFragment.newInstance(1))
                            .commit();
                    return true;
                case R.id.projects:
                    mTitle = "Projects";
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, ProjectsFragment.newInstance(2))
                            .commit();
                    return true;
                case R.id.nav_logout:
                    ParseUser.logOut();
                    navigateToMain();
                default:
                    mTitle = "Twitter";
                    mFragmentManager.beginTransaction()
                            .replace(R.id.container, HomeFragment.newInstance(3))
                            .commit();
                    return true;
            }
        }
    };

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);

        mNavigationView.setNavigationItemSelectedListener(mNavigationItemSelectedListener);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setTwitterAsDefault();
    }

    private void setTwitterAsDefault() {
        mFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance(0))
                .commit();
        // TODO - fix this
        mNavigationView.setCheckedItem(0);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.drawer_twitter);
                break;
            case 2:
                mTitle = getString(R.string.drawer_calendar);
                break;
            case 3:
                mTitle = getString(R.string.drawer_projects);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ParseUser.getCurrentUser() != null) {
            getMenuInflater().inflate(R.menu.menu_home, menu);
            restoreActionBar();
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

        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                ParseUser.logOut();
                navigateToMain();
        }

        return super.onOptionsItemSelected(item);
    }

    // start main activity after logout
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "You have been logged out.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFragmentInteraction(View view) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}