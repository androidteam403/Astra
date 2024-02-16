package com.thresholdsoft.astra.utils;

import android.content.Context;
import android.util.AttributeSet;

public class NoImeEditText extends androidx.appcompat.widget.AppCompatEditText {

    public NoImeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return false;
    }
}
