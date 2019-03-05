package com.baseproject.utils.pref;

import android.content.Context;

import com.baseproject.R;

import java.util.HashMap;

public class SessionManager {
    private SecurePreferences pref;

    public static SessionManager sessionManager;

    public static SessionManager getInstance(Context context) {
        if(sessionManager == null){
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }


    public SessionManager(Context context) {
        String PREF_NAME = context.getResources().getString(R.string.app_name);
        pref = SecurePreferences.getInstance(context, PREF_NAME);
    }

    public void storeUserData(HashMap<String, String> userDetail) {
        for (String key : userDetail.keySet()) {
            pref.putString(key, userDetail.get(key));
        }
        pref.commit();
    }

    /**
     * Getting value for key from shared Preferences
     *
     * @param key          key for which we need to get Value
     * @param defaultValue default value to be returned if key is not exits
     * @return It will return value of key if exist and defaultValue otherwise
     */
    public String getValueFromKey(String key, String defaultValue) {
        if (pref.containsKey(key)) {
            return pref.getString(key, defaultValue);
        } else {
            return defaultValue;
        }
    }


    /**
     * Setting value for key from shared Preferences
     *
     * @param key   key for which we need to get Value
     * @param value value for the key
     */
    public void setValueFromKey(String key, String value) {
        pref.putString(key, value);
    }


    /**
     * Setting value for key from shared Preferences
     *
     * @param key   key for which we need to get Value
     * @param value value for the key
     */
    public void setFlageFromKey(String key, boolean value) {
        pref.putBoolean(key, value);
    }


    /**
     * To get Flag from sharedPreferences
     *
     * @param key key of flag to get
     * @return flag value for key if exist. false if not key not exist.
     */
    public boolean getFlagFromKey(String key) {
        return pref.containsKey(key) && pref.getBoolean(key, false);
    }


}
