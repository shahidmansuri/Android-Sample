package com.baseproject;

import android.support.multidex.MultiDexApplication;

import com.baseproject.network.NetworkCall;
import com.baseproject.network.listeners.DefaultActionPerformer;
import com.baseproject.utils.Logger;

import java.util.HashMap;

/**
 * Created by Mushahid on 1/8/2018
 */

public class AppClass extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkCall.setIsDubuggable(BuildConfig.DEBUG);
        Logger.setLoggingEnabled(BuildConfig.DEBUG);

        NetworkCall.setBASE_URL(AppConstants.BASE_URL);
        NetworkCall.setActionPerformer(new DefaultActionPerformer() {
            @Override
            public void onActionPerform(HashMap<String, String> headers, HashMap<String, String> params) {

            }
        });
    }
}
