package com.example.samuel.ugcc.helper;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton MySingleton;
    private RequestQueue requestQueue;
    private static Context mctx;
    private MySingleton(Context context){
        this.mctx=context;
        this.requestQueue=getRequestQueue();

    }
    public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized MySingleton getInstance(Context context){
        if (MySingleton==null){
            MySingleton=new MySingleton(context);
        }
        return MySingleton;
    }
//    public<T> void addToRequestQue(Request<T> request){
//        requestQueue.add(request);
//
//    }

    public void addToRequestQueue(JsonObjectRequest jsArrayRequest) {
        requestQueue.add(jsArrayRequest);
    }
}