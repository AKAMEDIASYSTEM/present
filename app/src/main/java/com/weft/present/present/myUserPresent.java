package com.weft.present.present;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class myUserPresent extends BroadcastReceiver {

    private static final String TAG = "akaPresentReceiver";
    private SharedPreferences sp;
    private String ts;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Inside broadcastReceiver");
        Long tsLong = System.currentTimeMillis() / 1000;
        ts = tsLong.toString();

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Log.d(TAG, "Phone unlocked " + ts);
            startActionUnlock(context);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d(TAG, "Phone locked " + ts);
            startActionLock(context);
        }
    }

    /**
     * Starts this service to perform action Unlock with the given parameters. If
     * the service is already performing a task this action will be queued.
     */
    public static void startActionUnlock(Context context) {
        Intent intent = new Intent(context, UploadIntentService.class);
        intent.setAction(context.getString(R.string.unlockAction));
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Lock with the given parameters. If
     * the service is already performing a task this action will be queued.
     */
    public static void startActionLock(Context context) {
        Intent intent = new Intent(context, UploadIntentService.class);
        intent.setAction(context.getString(R.string.lockAction));
        context.startService(intent);
    }

}