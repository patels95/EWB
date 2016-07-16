package com.patels95.sanam.ewb;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetui.TweetUi;

import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    // Twitter Keys
    private static final String TWITTER_KEY = "a1vsMbocLKBlZqLRzroibhQXZ";
    private static final String TWITTER_SECRET = "RiLzY549zv60JCf586iZ7AmCj7TrbLQSqwNvHJWPfZKlWE2rLV";

    // Parse Keys
    private static final String parseAppId = "6w4hqUBySmTvypIDoGOSKO0WQ5hNfJKZUsTcunfH";
    private static final String parseClientId = "htBr1Ti6Wyi9rZIdBYo8gwrzLeHsxWHvfea1obVY";

    public void onCreate(){
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetUi());
        Parse.enableLocalDatastore(this);
//        Parse.initialize(this, parseAppId, parseClientId);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(parseAppId)
                .clientKey(parseClientId)
                .server("https://ewb-android.herokuapp.com/parse/").build());
        ParseUser.enableRevocableSessionInBackground();
    }
}
