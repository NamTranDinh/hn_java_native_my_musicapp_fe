package com.aptech.mymusic.utils;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.aptech.mymusic.application.App;
import com.mct.components.utils.ScreenUtils;

public class BarsUtils {

    ///////////////////////////////////////////////////////////////////////////
    // Status bar
    ///////////////////////////////////////////////////////////////////////////

    public static void setAppearanceLightStatusBars(Activity activity, boolean isLight) {
        if (activity != null) {
            setAppearanceLightStatusBars(activity.getWindow(), isLight);
        }
    }

    public static void setAppearanceLightStatusBars(Window window, boolean isLight) {
        if (window == null) {
            return;
        }
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(window, window.getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(isLight);
    }

    public static void offsetStatusBar(@NonNull ViewGroup v) {
        offsetStatusBar(v, ScreenUtils.getStatusBarHeight(App.getInstance()));
    }

    public static void offsetStatusBar(@NonNull ViewGroup v, int offset) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        lp.topMargin = offset;
        v.setLayoutParams(lp);
    }

}
