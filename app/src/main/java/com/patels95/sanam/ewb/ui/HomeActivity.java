package com.patels95.sanam.ewb.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.model.ParseConstants;

public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        HomeFragment.OnFragmentInteractionListener,
        CalendarFragment.OnFragmentInteractionListener,
        ProjectsFragment.OnFragmentInteractionListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = "Twitter";

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        // Fragment fragment = new CalendarFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                mTitle = "Twitter";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                mTitle = "Calendar";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CalendarFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                mTitle = "Projects";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ProjectsFragment.newInstance(position + 1))
                        .commit();
                break;
            default:
                mTitle = "Twitter";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance(position + 1))
                        .commit();
                break;
        }
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
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
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            if (ParseUser.getCurrentUser() != null) {
                getMenuInflater().inflate(R.menu.menu_home, menu);
                restoreActionBar();
                return true;
            }
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