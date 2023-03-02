package com.aptech.mymusic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.renderscript.RSRuntimeException;

import androidx.annotation.NonNull;

import jp.wasabeef.picasso.transformations.internal.FastBlur;
import jp.wasabeef.picasso.transformations.internal.RSBlur;

public class BitmapUtils {


    public static Bitmap blur(Context context, @NonNull Bitmap source, int radius, int sampling) {

        int scaledWidth = source.getWidth() / sampling;
        int scaledHeight = source.getHeight() / sampling;

        Bitmap bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) sampling, 1 / (float) sampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(source, 0, 0, paint);

        try {
            bitmap = RSBlur.blur(context, bitmap, radius);
        } catch (RSRuntimeException e) {
            bitmap = FastBlur.blur(bitmap, radius, true);
        }

        return bitmap;
    }

    private BitmapUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

}