package com.aptech.mymusic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
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

    public static Bitmap roundedCorner(@NonNull Bitmap bitmap, int cornerRadius) {
        // Create a new bitmap with the same dimensions as the original bitmap
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a new canvas and associate it with the new bitmap
        Canvas canvas = new Canvas(output);

        // Create a new paint object and set its antiAlias and filter flags to true
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        // Create a new rect object with the same dimensions as the bitmap
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        // Create a new rectF object with the same dimensions as the bitmap
        RectF rectF = new RectF(rect);

        // Create a new BitmapShader using the original bitmap and the CLAMP tile mode
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        // Set the shader of the paint object to the BitmapShader
        paint.setShader(bitmapShader);

        // Draw a round rect using the paint object and the rectF object
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

        // Recycle the original bitmap to free up memory
        // bitmap.recycle();

        // Return the bitmap with rounded corners
        return output;
    }

    private BitmapUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

}