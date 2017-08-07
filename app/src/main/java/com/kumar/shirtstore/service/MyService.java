package com.kumar.shirtstore.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Purushotham on 07/08/17.
 */

public class MyService extends IntentService {
    int counter = 0;

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "MyServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "MyServicePayload";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Uri uri = intent.getData();

        Log.i(TAG, "onHandleIntent: " + counter + " "+ uri.toString());

        try {
            Thread.sleep(1000);
            counter++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, "Service all done!");
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
