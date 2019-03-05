package com.baseproject.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.baseproject.R;

import java.lang.reflect.Field;


@SuppressLint("Recycle")
public class CustomTextInputLayout extends TextInputLayout {


    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyDefaultFont(context);

        initFont(context,attrs);
    }

    private void initFont(Context context,AttributeSet attrs) {
        FontManager.getInstance().applyFont(getEditText(), attrs);

        try {
            // Retrieve the CollapsingTextHelper Field
            final Field cthf = TextInputLayout.class.getDeclaredField("mCollapsingTextHelper");
            cthf.setAccessible(true);

            // Retrieve an instance of CollapsingTextHelper and its TextPaint
            final Object cth = cthf.get(this);
            final Field tpf = cth.getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            // Apply your Typeface to the CollapsingTextHelper TextPaint
            applyFont((TextPaint) tpf.get(cth), attrs,context);
        } catch (Exception ignored) {
            // Nothing to do
        }

    }


    public void applyFont(TextPaint textView, AttributeSet attrs,Context context) {
        if (attrs != null) {
            TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.customFont, 0, 0);
            String fontPath = styledAttributes.getString(R.styleable.customFont_fontFamily);
            if (!TextUtils.isEmpty(fontPath)) {
                Typeface typeface = FontManager.getInstance().getTypeface(context, fontPath);
                if (typeface != null) {
                    textView.setTypeface(typeface);
                }
            }
            styledAttributes.recycle();
        }
    }

    private void applyDefaultFont(Context context) {
        Typeface typeface = FontManager.getInstance().getTypeface(context, context.getString(R.string.font_regular));
        if (typeface != null) {
            this.setTypeface(typeface);
        }

        try {
            // Retrieve the CollapsingTextHelper Field
            final Field cthf = TextInputLayout.class.getDeclaredField("mCollapsingTextHelper");
            cthf.setAccessible(true);

            // Retrieve an instance of CollapsingTextHelper and its TextPaint
            final Object cth = cthf.get(this);
            final Field tpf = cth.getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            // Apply your Typeface to the CollapsingTextHelper TextPaint
            ((TextPaint) tpf.get(cth)).setTypeface(typeface);
        } catch (Exception ignored) {
            // Nothing to do
        }

    }




}
