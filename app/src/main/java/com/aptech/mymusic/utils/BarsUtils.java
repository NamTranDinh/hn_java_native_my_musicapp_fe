package com.aptech.mymusic.utils;

import android.app.Activity;
import android.view.Window;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

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

}
