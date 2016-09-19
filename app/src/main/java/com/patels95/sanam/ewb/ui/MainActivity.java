package com.patels95.sanam.ewb.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.patels95.sanam.ewb.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String IS_MEMBER = "IS_MEMBER";
    private boolean mIsMember = false;

    @BindView(R.id.tool_bar) Toolbar mToolbar;
    @BindView(R.id.memberButton) Button mMember;
    @BindView(R.id.guestButton) Button mGuest;
    @BindView(R.id.backLabel) TextView mBack;
    @BindView(R.id.inputEmail) EditText mEmail;
    @BindView(R.id.inputPassword) EditText mPassword;
    @BindView(R.id.forgotPassword) TextView mForgotPassword;
    @BindView(R.id.submitLogin) Button mSubmitLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d(TAG, "toolbar: " + mToolbar);
        Log.d(TAG, "member: " + mMember);
        setSupportActionBar(mToolbar);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            memberStartHome();
        }

        //login is invisible by default
        toggleLogin();

        mGuest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                guestStartHome();
            }
        });

        mMember.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                toggleLogin();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLogin();
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mEmail.getText().length() == 0){
                    String msg = "Please enter your email then press \"forgot password\"";
                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    ParseUser.requestPasswordResetInBackground(mEmail.getText().toString(), new RequestPasswordResetCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                //display error message
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            else{
                                //display "reset password" email sent confirmation
                                String resetConfirmation = "A reset password link has been send to your email";
                                Toast.makeText(MainActivity.this, resetConfirmation, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        mSubmitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validationError = validate();
                //no error -> login user
                if(!validationError){
                    //Set up a progress dialog
                    final ProgressDialog dlg = new ProgressDialog(MainActivity.this);
                    dlg.setTitle("Please wait.");
                    dlg.setMessage("Logging In...");
                    dlg.show();

                    //call the Parse login method
                    ParseUser.logInInBackground(mEmail.getText().toString(), mPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            dlg.dismiss();
                            if(e != null){
                                //display error message
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            else{
                                //start the home activity as an ewb member
                                memberStartHome();
                            }
                        }
                    });

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_register) {
            startRegister();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void memberStartHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        mIsMember = true;
        intent.putExtra(IS_MEMBER, mIsMember);
        startActivity(intent);
    }

    private boolean validate() {
        boolean validationError = false;
        StringBuilder validationErrorMsg = new StringBuilder("Please ");

        if(mEmail.getText().toString().length() == 0){
            validationError = true;
            validationErrorMsg.append("enter your email");
        }
        if(mPassword.getText().toString().length() == 0){
            if(validationError) {
                validationErrorMsg.append(", and ");
            }
            validationError = true;
            validationErrorMsg.append("enter your password");
        }
        if(validationError){
            Toast.makeText(this, validationErrorMsg.toString(), Toast.LENGTH_LONG).show();
        }

        return validationError;
    }

    private void startRegister() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void toggleLogin() {
        if(mEmail.getVisibility() == View.VISIBLE){
            mEmail.setVisibility(View.INVISIBLE);
            mPassword.setVisibility(View.INVISIBLE);
            mForgotPassword.setVisibility(View.INVISIBLE);
            mSubmitLogin.setVisibility(View.INVISIBLE);
            mBack.setVisibility(View.INVISIBLE);
            mMember.setVisibility(View.VISIBLE);
            mGuest.setVisibility(View.VISIBLE);
        }
        else{
            mEmail.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.VISIBLE);
            mForgotPassword.setVisibility(View.VISIBLE);
            mSubmitLogin.setVisibility(View.VISIBLE);
            mBack.setVisibility(View.VISIBLE);
            mMember.setVisibility(View.INVISIBLE);
            mGuest.setVisibility(View.INVISIBLE);
        }
    }

    private void guestStartHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        mIsMember = false;
        intent.putExtra(IS_MEMBER, mIsMember);
        startActivity(intent);
    }

    private void openTwitter() {
        String url = "https://twitter.com/EWBBU";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void openEWB(){
        String url = "http://www.ewbbu.com/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void openFacebook(){
        String url = "https://www.facebook.com/buewb";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }




}
