package com.patels95.sanam.ewb;

import android.content.Intent;
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

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.memberButton) Button mMember;
    @InjectView(R.id.guestButton) Button mGuest;
    @InjectView(R.id.registerLabel) TextView mRegister;
    @InjectView(R.id.facebookIcon) ImageView mFacebook;
    @InjectView(R.id.twitterIcon) ImageView mTwitter;
    @InjectView(R.id.ewbWebsite) TextView mWebsite;
    @InjectView(R.id.userLabel) TextView mUserLabel;
    @InjectView(R.id.inputEmail) EditText mEmail;
    @InjectView(R.id.inputPassword) EditText mPassword;

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
                ewbClick();
            }
        });

        mFacebook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                facebookClick();
            }
        });

        mTwitter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                twitterClick();
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

    }

    private void toggleLogin() {
        if(mEmail.getVisibility() == View.VISIBLE){
            mEmail.setVisibility(View.INVISIBLE);
            mPassword.setVisibility(View.INVISIBLE);
            mUserLabel.setVisibility(View.VISIBLE);
            mMember.setVisibility(View.VISIBLE);
            mGuest.setVisibility(View.VISIBLE);
        }
        else{
            mEmail.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.VISIBLE);
            mUserLabel.setVisibility(View.INVISIBLE);
            mMember.setVisibility(View.INVISIBLE);
            mGuest.setVisibility(View.INVISIBLE);
        }
    }

    private void guestStartHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void twitterClick() {
        String url = "https://twitter.com/EWBBU";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void ewbClick(){
        String url = "http://www.ewbbu.com/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void facebookClick(){
        String url = "https://www.facebook.com/buewb";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }




}
