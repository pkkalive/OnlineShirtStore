package com.kumar.shirtstore.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kumar.shirtstore.model.CartItems;
import com.kumar.shirtstore.utils.HttpHelper;
import com.kumar.shirtstore.utils.RequestPackage;

import java.io.IOException;

/**
 * Created by Purushotham on 07/08/17.
 */

public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "MyServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "MyServicePayload";
    public static final String REQUEST_PACKAGE = "requestPackage";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        RequestPackage requestPackage = (RequestPackage)
                intent.getParcelableExtra(REQUEST_PACKAGE);
        String response = null;
        try {

            if (HttpHelper.responseCode == 500){
                Toast.makeText(this,
                        "Server error. Please try again. Sorry for the inconvenience",
                        Toast.LENGTH_LONG).show();
            } else {
                response = HttpHelper.downloadFromFeed(requestPackage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Gson gson = new Gson();
        if (response != null) {
            CartItems[] cartItems = gson.fromJson(response, CartItems[].class);

            Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
            messageIntent.putExtra(MY_SERVICE_PAYLOAD, cartItems);
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
        } else {
            Toast.makeText(this,
                    "Server error. Please try again. Sorry for the inconvenience",
                    Toast.LENGTH_LONG).show();
        }
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