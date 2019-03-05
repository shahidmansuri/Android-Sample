package com.baseproject.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.baseproject.R;

@SuppressLint("Recycle")
public class CustomEditText extends AppCompatEditText {
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyDefaultFont(context);
        FontManager.getInstance().applyFont(this, attrs);
    }


    private void applyDefaultFont(Context context) {
        Typeface typeface = FontManager.getInstance().getTypeface(context, context.getString(R.string.font_regular));
        if (typeface != null) {
            this.setTypeface(typeface);
        }
    }

}
