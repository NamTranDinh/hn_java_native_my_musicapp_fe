package com.aptech.mymusic.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aptech.mymusic.R;
import com.aptech.mymusic.application.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.function.Consumer;

public class GlideUtils {

    public static void load(String url, ImageView target) {
        load(url, target, R.drawable.ic_placeholder, R.drawable.ic_placeholder_error);
    }

    public static void load(String url, ImageView target, int placeholderRes, int errorRes) {
        Request().load(url)
                .placeholder(placeholderRes)
                .error(errorRes)
                .into(target);
    }

    public static void load(String url, Consumer<Bitmap> onDone) {
        load(url, onDone, new Handler(Looper.getMainLooper()));
    }

    public static void load(String url, Consumer<Bitmap> onDone, Handler handler) {
        Request().asBitmap().load(url)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        handler.post(() -> onDone.accept(null));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        handler.post(() -> onDone.accept(resource));
                        return true;
                    }
                }).submit();
    }

    @NonNull
    private static RequestManager Request() {
        return Glide.with(App.getInstance());
    }
}
