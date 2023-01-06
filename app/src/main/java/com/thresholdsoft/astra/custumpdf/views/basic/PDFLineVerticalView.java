package com.thresholdsoft.astra.custumpdf.views.basic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public class PDFLineVerticalView extends PDFView {
    public PDFLineVerticalView(Context context) {
        super(context);

        View separatorLine = new View(context);
        LinearLayout.LayoutParams separatorLayoutParam = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
        separatorLine.setPadding(0, 0, 5, 5);
        separatorLine.setLayoutParams(separatorLayoutParam);

        super.setView(separatorLine);
    }

    @Override
    protected PDFLineSeparatorView addView(@NonNull PDFView viewToAdd) throws IllegalStateException {
        throw new IllegalStateException("Cannot add subview to Line Separator");
    }

    @Override
    public PDFLineVerticalView setLayout(@NonNull ViewGroup.LayoutParams layoutParams) {
        super.setLayout(layoutParams);
        return this;
    }

    public PDFLineVerticalView setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        return this;
    }
}
