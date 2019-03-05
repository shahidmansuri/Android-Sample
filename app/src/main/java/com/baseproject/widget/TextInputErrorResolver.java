package com.baseproject.widget;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextInputErrorResolver implements TextWatcher {
    private EditText editText;
    private AppCompatEditText compatEditText;
    private TextInputLayout textInputLayout;

    public TextInputErrorResolver(EditText editText, TextInputLayout textInputLayout) {
        this.editText = editText;
        this.textInputLayout = textInputLayout;
    }

    public TextInputErrorResolver(AppCompatEditText compatEditText, TextInputLayout textInputLayout) {
        this.compatEditText = compatEditText;
        this.textInputLayout = textInputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (editText != null) {
            if (!editText.getText().toString().isEmpty()) {
                this.textInputLayout.setError(null);
                this.textInputLayout.setErrorEnabled(false);
            }
        }
        if (compatEditText != null) {
            if (!compatEditText.getText().toString().isEmpty()) {
                this.textInputLayout.setError(null);
                this.textInputLayout.setErrorEnabled(false);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
