package com.weft.present.present;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


public class UploadIntentService extends IntentService {

//    private static final String ACTION_UNLOCK = this.getString(R.string.unlockAction);
//    private static final String ACTION_LOCK = getString(R.string.lockAction);
    private static final String TAG = "AKAUploadIntentService";

    private SharedPreferences sp;

    public UploadIntentService() {
        super("UploadIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (this.getString(R.string.unlockAction).equals(action)) {

                updateLockEvent(this, false);
            } else if (this.getString(R.string.lockAction).equals(action)) {
                updateLockEvent(this, true);
            }
        }
    }

    void updateLockEvent(Context c, boolean isLock){
        Log.d(TAG, "Inside IntentService::updateLockEvent");
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        sp = c.getSharedPreferences(c.getString(R.string.spName), Context.MODE_PRIVATE);
        String key = sp.getString( c.getString(R.string.userKey), "none");
        String username = sp.getString( c.getString(R.string.username), "none");
        Log.d(TAG, "userKey is "+key);
        Log.d(TAG, "username is "+username);
        RequestParams rp = new RequestParams("x-aio-key", key);
        rp.put("value", ts);
        String feed = "";
        if(isLock){
            String fv = sp.getString( c.getString(R.string.lockFeed), "");
            if(!fv.isEmpty()) {
                feed = c.getString(R.string.adafruit_io_endpoint) + username + "/feeds/" + fv + "/data";
            }
        } else {
            String fv = sp.getString( c.getString(R.string.unlockFeed), "");
            if(!fv.isEmpty()) {
                feed = c.getString(R.string.adafruit_io_endpoint) + username + "/feeds/" + fv + "/data";
            }
        }
        Log.d(TAG, "feed is "+feed);
        SyncHttpClient mclient = new SyncHttpClient();
        mclient.post(feed, rp, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                // Initiated the request
                Log.i(TAG, "AKA upload started");
            }

            @Override
            public void onSuccess(int responseCode, Header[] headers, String responseBody) {
                // Successfully got a response
                Log.d(TAG, "AKA upload Success: " + responseBody);
            }

            @Override
            public void onFailure(int responseCode, Header[] headers, String responseBody, Throwable e) {
                // Response failed :(
                Log.d(TAG, "AKA Present uploading Failure: " + responseBody);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "reached AKA onFinish Present in the myUserPresent IntentService");
                // Completed the request (either success or failure)
                // maybe do a Toast to show what's up?
            }

        });
    }

}
