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
    // Sanam's Twitter
//    private static final String TWITTER_KEY = "OuZb4XNtiRxHdxjPdEXkx1cTx";
//    private static final String TWITTER_SECRET = "r581PWY4ESS5mmARjzLSP0NfTyox0uxKL1eZgJhCeJB2F1Gtrb";

    // Daniel's Twitter
    private static final String TWITTER_KEY = "a1vsMbocLKBlZqLRzroibhQXZ";
    private static final String TWITTER_SECRET = "RiLzY549zv60JCf586iZ7AmCj7TrbLQSqwNvHJWPfZKlWE2rLV";

    // Daniel's parse keys
//    private static final String parseAppId = "KIlFW595YoyCLbh65tBvuFmdmXHGwDGLeJemfYJ5";
//    private static final String parseClientId = "DuM4muyMVNoz7eNlTo0xolkyfD1yM0BrWcbZYRx5";

    // Sanam's Parse Keys
    private static final String parseAppId = "6w4hqUBySmTvypIDoGOSKO0WQ5hNfJKZUsTcunfH";
    private static final String parseClientId = "htBr1Ti6Wyi9rZIdBYo8gwrzLeHsxWHvfea1obVY";

    public void onCreate(){
        // Sanam's Twitter keys for fabric:
        // twitter_key: "OuZb4XNtiRxHdxjPdEXkx1cTx"
        // twitter_secret: "r581PWY4ESS5mmARjzLSP0NfTyox0uxKL1eZgJhCeJB2F1Gtrb"
        // Sanam's Parse keys:
        // app_id: swbDtaPTGNfZ533isw8IFLwNwufY78HjeXFQRN0i
        // client_id: Jx9sBzGV9XAFkAyv6kSXYtztHCOcuuCAt0hVKdQd
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetUi());
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, parseAppId, parseClientId); // Change Parse keys here.
        ParseUser.enableRevocableSessionInBackground();
    }
}
