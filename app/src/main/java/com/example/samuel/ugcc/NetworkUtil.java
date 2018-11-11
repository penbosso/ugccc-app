package com.example.samuel.ugcc;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by krogers on 4/23/18.
 */


public class NetworkUtil {
    //variables of the class
    private static NetworkUtil mInstance;
    private static Context mCtx;
    private RequestQueue requestQueue;
    public static final String URL = "http://192.168.43.108/";

    //constructor to start the networking operation
    private NetworkUtil(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    //get an Instance of this class to perform a network operation
    public static synchronized NetworkUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkUtil(context);
        }
        return mInstance;
    }

    //get the request queue
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    //adding a request to the request queue
    public <T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }
}