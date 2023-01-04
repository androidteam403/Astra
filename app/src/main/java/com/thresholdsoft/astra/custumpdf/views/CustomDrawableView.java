package com.thresholdsoft.astra.custumpdf.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.view.View;

public class CustomDrawableView extends View {

    Paint fgPaintSel;

    public CustomDrawableView(Context context) {
        super(context);
        fgPaintSel = new Paint();
        fgPaintSel.setAlpha(255);
        fgPaintSel.setStrokeWidth(2);
        fgPaintSel.setColor(Color.CYAN);
        fgPaintSel.setStyle(Paint.Style.FILL_AND_STROKE);
        fgPaintSel.setPathEffect(new DashPathEffect(new float[]{2, 4}, 50));
    }

    protected void onDraw(Canvas canvas) {


        canvas.drawLine(100, 500, 100, 300, fgPaintSel);

    }

}