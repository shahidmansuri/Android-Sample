package com.baseproject.network.listeners;

import org.json.JSONObject;


public interface RetrofitResponseListener {
    void onPreExecute();
    void onSuccess(int statusCode, JSONObject jsonObject, String response);
    void onError(int statusCode, String message);
}
