package com.patels95.sanam.ewb;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private Button mMember;
    private Button mGuest;
    private TextView mRegister;
    private ImageView mFacebook;
    private ImageView mTwitter;
    private TextView mWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMember = (Button) findViewById(R.id.memberButton);
        mGuest = (Button) findViewById(R.id.guestButton);
        mRegister = (TextView) findViewById(R.id.registerLabel);
        mFacebook = (ImageView) findViewById(R.id.facebookIcon);
        mTwitter = (ImageView) findViewById(R.id.twitterIcon);
        mWebsite = (TextView) findViewById(R.id.ewbWebsite);

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
