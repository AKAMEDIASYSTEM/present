package com.weft.present.present;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class myUserPresent extends BroadcastReceiver {

    private static final String TAG = "akaPresentReceiver";
    private SharedPreferences sp;
    private String ts;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Inside broadcastReceiver");
        Long tsLong = System.currentTimeMillis()/1000;
        ts = tsLong.toString();

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            Log.d(TAG, "Phone unlocked "+ts);
            updateLockEvent(context, false);
        }else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Log.d(TAG, "Phone locked "+ts);
            updateLockEvent(context, true);
        }
    }

    void updateLockEvent(Context c, boolean isLock){
        Log.d(TAG, "Inside broadcastReceiver::updateLockEvent");
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
        AsyncHttpClient mclient = new AsyncHttpClient();
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
                Log.i(TAG, "reached AKA onFinish Present in the myUserPresent BroadcastReceiver");
                // Completed the request (either success or failure)
                // maybe do a Toast to show what's up?
            }

        });
    }
}
