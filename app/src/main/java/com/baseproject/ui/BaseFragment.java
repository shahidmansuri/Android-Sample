package com.baseproject.ui;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.baseproject.utils.pref.SessionManager;


/**
 * Each Fragment must extends BaseFragment.
 * Also each fragment must used the mainView already declared in BaseFragment for Container View.
 */
public class BaseFragment extends Fragment {


    protected Context mContext;
    public Activity mActivity;

    // Preference Handeling
    protected SessionManager session;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        if (context instanceof Activity) {
            mActivity = (Activity) context;

        }
        session = SessionManager.getInstance(mContext);
    }





    public boolean onBackPressed(){
        return false;
    }


}

