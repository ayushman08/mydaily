package com.smartdata.utils;

/**
 * Created by Vishwanath Nahak on 7/12/2017.
 */

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

public class TextDrawable extends Drawable {
    private static final int DEFAULT_COLOR = Color.WHITE;
    private static final int DEFAULT_TEXTSIZE = 15;
    private Paint mPaint;
    private CharSequence mText;
    private int mIntrinsicWidth;
    private int mIntrinsicHeight;

    public TextDrawable(CharSequence text) {
        mText = text;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setTextSize(65);
        mIntrinsicWidth = (int) (mPaint.measureText(mText, 0, mText.length()) + .5);
        mIntrinsicHeight = mPaint.getFontMetricsInt(null);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawText(mText, 0, mText.length(),
                bounds.centerX(), bounds.centerY(), mPaint);

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return mIntrinsicWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mIntrinsicHeight;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
        mPaint.setColorFilter(filter);
    }

}
