package com.aptech.mymusic.presentation.view.dialog;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.aptech.mymusic.R;
import com.mct.components.baseui.BaseOverlayDialog;

public class LoadingDialog extends BaseOverlayDialog {

    private final OnBackPressListener listener;

    public LoadingDialog(@NonNull Context context, OnBackPressListener listener) {
        super(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        return new AlertDialog.Builder(context).setCancelable(false).setView(R.layout.dialog_loading);
    }

    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog) {
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (listener != null) {
                listener.onBackPress(this);
                return true;
            }
            return false;
        });
    }


    @Override
    protected int getBackgroundColor() {
        return Color.TRANSPARENT;
    }

    public interface OnBackPressListener {
        void onBackPress(BaseOverlayDialog dialog);
    }
}
