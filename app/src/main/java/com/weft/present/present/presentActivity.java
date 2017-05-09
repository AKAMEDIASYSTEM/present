package com.weft.present.present;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class presentActivity extends AppCompatActivity {

    SharedPreferences sp;
    private static final String TAG = "AKA presentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "starting up");
        sp = getSharedPreferences(this.getString(R.string.spName), Context.MODE_PRIVATE);
        String userKey = sp.getString(this.getString(R.string.userKey), "");
        String username = sp.getString(this.getString(R.string.username), "");
        String lockFeed = sp.getString(this.getString(R.string.lockFeed), "");
        String unlockFeed = sp.getString(this.getString(R.string.unlockFeed), "");
        Log.d(TAG, "userKey is "+ userKey);
        Log.d(TAG, "username is "+ username);
        Log.d(TAG, "lockFeed is "+ lockFeed);
        Log.d(TAG, "unlockFeed is "+ unlockFeed);
        setContentView(R.layout.activity_present);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCreds();
                Log.d(TAG, "click on save button");
                Snackbar.make(view, "OK, I think I saved that", Snackbar.LENGTH_LONG)
                        .setAction("YUH", null).show();
            }
        });

        FloatingActionButton fabq = (FloatingActionButton) findViewById(R.id.fabQuit);
        fabq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click on Quit button");
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });

        if(!userKey.isEmpty()){
            EditText edk = (EditText)findViewById(R.id.editText);
            edk.setText(userKey);
        }
        if(!username.isEmpty()){
            EditText edk = (EditText)findViewById(R.id.editText4);
            edk.setText(username);
        }
        if(!lockFeed.isEmpty()){
            EditText edk = (EditText)findViewById(R.id.editText2);
            edk.setText(lockFeed);
        }
        if(!unlockFeed.isEmpty()){
            EditText edk = (EditText)findViewById(R.id.editText3);
            edk.setText(unlockFeed);
        }

        IntentFilter ifl = new IntentFilter();
        ifl.addAction(Intent.ACTION_USER_PRESENT);
        ifl.addAction((Intent.ACTION_SCREEN_OFF));
        registerReceiver(new myUserPresent(), ifl);

    }

    void saveCreds(){
        // save user's Adafruit IO API key
        // and feed labels
        EditText ed1 = (EditText)findViewById(R.id.editText);
        EditText ed2 = (EditText)findViewById(R.id.editText2);
        EditText ed3 = (EditText)findViewById(R.id.editText3);
        EditText ed4 = (EditText)findViewById(R.id.editText4);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getString(R.string.userKey), ed1.getText().toString());
        editor.putString(getString(R.string.lockFeed), ed2.getText().toString());
        editor.putString(getString(R.string.unlockFeed), ed3.getText().toString());
        editor.putString(getString(R.string.username), ed4.getText().toString());
        editor.commit();
        Log.d(TAG, "Saved to sp "+ed1.getText().toString());
    }

}
