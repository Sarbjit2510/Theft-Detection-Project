package com.pragyaware.sarbjit.jkpddapp.mUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by hina on 5/26/2017.
 */

public class FontTextViewRegular extends TextView {


    public FontTextViewRegular(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/ClearSans-Regular.ttf");
        this.setTypeface(face);
    }

    public FontTextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/ClearSans-Regular.ttf");
        this.setTypeface(face);
    }

    public FontTextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/ClearSans-Regular.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }

}
