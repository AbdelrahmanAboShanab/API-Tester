package com.example.apitester.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.apitester.pojo.Post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostLoader extends AsyncTaskLoader<Integer> {

    private String mUrl;
    int method;
    ArrayList<String> headers;
    HashMap<String,String> data;
    public PostLoader(@NonNull Context context, String url, int method, ArrayList<String> headers) {
        super(context);
        Log.i("TAG", "m5: ");
        this.mUrl = url;
        this.method = method;
        this.headers = headers;
    }
    public PostLoader(@NonNull Context context, String url, int method,
                      ArrayList<String> headers, HashMap<String,String> data) {
        super(context);
        Log.i("TAG", "m5: ");
        this.mUrl = url;
        this.method = method;
        this.headers = headers;
        this.data = data;
    }

    @Override
    protected void onStartLoading() {
        Log.i("TAG", "m6: ");
        forceLoad();
    }

    @Nullable
    @Override
    public Integer loadInBackground() {
        if (mUrl == null) {
            return -1;
        }

        Log.i("TAG", "m7: ");
            if(method == 1) {
                int response = 0;
                try {
                    response = QueryUtils.makeGetHttpRequest(mUrl, method, headers);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            }else{
                int response = 0;
                try {
                    response = QueryUtils.makePostHttpRequest(mUrl, method, headers,data);
                 } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            }

    }
}
