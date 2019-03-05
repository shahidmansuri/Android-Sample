package com.baseproject.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.baseproject.utils.UniversalImageLoader;
import com.baseproject.utils.pref.SessionManager;
import com.baseproject.utils.CommonDialogs;
import com.baseproject.utils.CustomProgressDialog;


/**
 * Each Activity must extends BaseActivity.
 * It is having some common implementation that each Application is having
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CODE = 8562;

    protected boolean shouldPerformDispatchTouch = true;
    protected SessionManager session;
    protected UniversalImageLoader universalImageLoader;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    // Progress
    private CustomProgressDialog progressDialog;

    // CommonDailogs
    protected CommonDialogs mDailogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = SessionManager.getInstance(this);
        mDailogs = new CommonDialogs(this);
        universalImageLoader = new UniversalImageLoader(BaseActivity.this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (shouldPerformDispatchTouch) {
            if (view instanceof EditText) {
                try {
                    View w = getCurrentFocus();
                    int scrcords[] = new int[2];
                    w.getLocationOnScreen(scrcords);
                    float x = event.getRawX() + w.getLeft() - scrcords[0];
                    float y = event.getRawY() + w.getTop() - scrcords[1];

                    if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (getWindow() != null && getWindow().getCurrentFocus() != null) {
                            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }


    /**
     * Show Progress Dailog without any message
     */
    public void showProgress() {
        showProgress(null);
    }

    /**
     * Show Progress dialog with Message
     *
     * @param message Message to show under Progress
     */
    public void showProgress(String message) {
        if (progressDialog != null) {
            progressDialog.show(message);
        } else {
            progressDialog = new CustomProgressDialog(this, message);
            progressDialog.show();
        }
    }


    /**
     * Stop Progress if running and dismiss progress Dialog
     */
    public void stopProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * To check weather the progress is running
     *
     * @return True if Progress is running false else.
     */
    protected boolean isProgressShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }

    /**
     * Check if the device is connected to internet
     *
     * @return true if connected to internet false otherwise.
     */
    protected boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isAvailable() && activeNetwork.isConnected();
    }


    protected void openAppPermissionSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, PERMISSION_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
