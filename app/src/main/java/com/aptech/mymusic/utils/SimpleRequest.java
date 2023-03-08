package com.aptech.mymusic.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.aptech.mymusic.R;
import com.aptech.mymusic.application.App;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class SimpleRequest implements RequestListener<Bitmap> {

    ImageView mTarget;

    public SimpleRequest(ImageView target) {
        this.mTarget = target;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
        Bitmap resource = BitmapFactory.decodeResource(mTarget.getResources(), R.drawable.custom_overlay_black);
        mTarget.setImageBitmap(BitmapUtils.blur(mTarget.getContext(), resource, 25, 1));
        return false;
    }

    @Override
    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
        mTarget.setImageBitmap(BitmapUtils.blur(mTarget.getContext(), resource, 25, 1));
        return false;
    }
}
