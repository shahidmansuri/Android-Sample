package com.baseproject.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

import com.baseproject.R;

@SuppressLint("Recycle")
public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
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
