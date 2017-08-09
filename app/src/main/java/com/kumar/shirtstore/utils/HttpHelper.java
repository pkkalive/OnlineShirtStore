package com.kumar.shirtstore.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.util.Log;
import android.widget.Toast;

import com.kumar.shirtstore.MainActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Purushotham on 07/08/17.
 */

/**
 * Helper class for working with a remote server
 */
public class HttpHelper {
    public static int responseCode;

    /**
     * Returns text from a URL on a web server
     *
     * @param requestPackage
     * @return
     * @throws IOException
     */
    public static String downloadFromFeed (RequestPackage requestPackage)
            throws IOException {

        String address = requestPackage.getEndpoint();
        String encodedParams = requestPackage.getEncodedParams();

        if (requestPackage.getMethod().equals("GET") &&
                encodedParams.length() > 0) {
            address = String.format("%s?%s", address, encodedParams);
        }

        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder()
                .url(address);

        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Exception: response code " + response.code());
        }
    }
}