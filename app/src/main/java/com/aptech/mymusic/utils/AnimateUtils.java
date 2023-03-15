package com.aptech.mymusic.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class AnimateUtils {

    public static void animateHeightAndDisappear(View view, int duration, int fromHeight, int toHeight) {
        // Create a value animator that gradually creases/decreases the height of the view
        ValueAnimator valueAnimator = ValueAnimator.ofInt(fromHeight, toHeight);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            // Get the animated value of the height and set it as the new height of the view
            view.getLayoutParams().height = (int) animation.getAnimatedValue();
            view.setLayoutParams(view.getLayoutParams());
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Show the view when the animation start
                if (fromHeight < toHeight) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Hide the view after the animation ends
                if (fromHeight > toHeight) {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        // Start the animation
        valueAnimator.start();
    }

    public static void animatePaddingTop(View view, int duration, int from, int to) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            // Get the animated value of the padding and set it as the new padding of the view
            view.setPadding(0, (int) animation.getAnimatedValue(), 0, 0);
        });
        // Start the animation
        valueAnimator.start();
    }
}
