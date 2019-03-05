package com.baseproject.utils;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baseproject.R;
public class CustomProgressDialog extends android.support.v7.app.AlertDialog {

    private String title;
    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, String title) {
        super(context);
        this.title = title;
        super.setCancelable(false);
    }


    public void show(String text){
        title = text;
        show();
    }

    @Override
    public void show() {

        try {
            super.show();
            super.setCanceledOnTouchOutside(false);

            if (title!=null && !TextUtils.isEmpty(title)) {
                setContentView(R.layout.custom_progressdialog);
                TextView tvTitle =  findViewById(R.id.progress_bar_text);
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
            } else {
                setContentView(R.layout.custom_progressdialog);
            }

            getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

}