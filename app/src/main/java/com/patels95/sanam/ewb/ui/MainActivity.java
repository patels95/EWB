package com.patels95.sanam.ewb.ui;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.patels95.sanam.ewb.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.memberButton) Button mMember;
    @InjectView(R.id.guestButton) Button mGuest;
    @InjectView(R.id.registerLabel) TextView mRegister;
    @InjectView(R.id.facebookIcon) ImageView mFacebook;
    @InjectView(R.id.twitterIcon) ImageView mTwitter;
    @InjectView(R.id.ewbWebsite) TextView mWebsite;
    @InjectView(R.id.inputEmail) EditText mEmail;
    @InjectView(R.id.inputPassword) EditText mPassword;
    @InjectView(R.id.forgotPassword) TextView mForgotPassword;
    @InjectView(R.id.submitLogin) Button mSubmitLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //login is invisible by default
        toggleLogin();

        mWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEWB();
            }
        });

        mFacebook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openFacebook();
            }
        });

        mTwitter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openTwitter();
            }
        });

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

        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startRegister();
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
                    dlg.setMessage("Signing Up. Please wait.");
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void memberStartHome() {
        Intent intent = new Intent(this, HomeActivity.class);
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
            mMember.setVisibility(View.VISIBLE);
            mGuest.setVisibility(View.VISIBLE);
        }
        else{
            mEmail.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.VISIBLE);
            mForgotPassword.setVisibility(View.VISIBLE);
            mSubmitLogin.setVisibility(View.VISIBLE);
            mMember.setVisibility(View.INVISIBLE);
            mGuest.setVisibility(View.INVISIBLE);
        }
    }

    private void guestStartHome() {
        Intent intent = new Intent(this, HomeActivity.class);
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
