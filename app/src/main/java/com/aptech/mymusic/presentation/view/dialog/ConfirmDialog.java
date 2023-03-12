package com.aptech.mymusic.presentation.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.aptech.mymusic.R;
import com.mct.components.baseui.BaseOverlayDialog;

public class ConfirmDialog extends BaseOverlayDialog {

    private final String title;
    private final String message;
    private final IOnClickListener onClickListener;

    public ConfirmDialog(@NonNull Context context, String message, IOnClickListener onClickListener) {
        this(context, "Confirmation", message, onClickListener);
    }

    public ConfirmDialog(@NonNull Context context, String title, String message, IOnClickListener onClickListener) {
        super(context);
        this.title = title;
        this.message = message;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
        initView(view);
        return new AlertDialog.Builder(context).setView(view);
    }

    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog) {
    }

    @Override
    protected int getCornerRadius() {
        return 4;
    }

    private void initView(@NonNull View view) {
        TextView tvTitle = view.findViewById(R.id.dialog_title);
        TextView tvMessage = view.findViewById(R.id.dialog_message);
        tvTitle.setText(title);
        tvMessage.setText(message);
        view.findViewById(R.id.dialog_cancel_button).setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClickCancel(this);
            } else {
                dismiss();
            }
        });
        view.findViewById(R.id.dialog_ok_button).setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClickOk(this);
            } else {
                dismiss();
            }
        });
    }

    public interface IOnClickListener {
        void onClickCancel(BaseOverlayDialog dialog);

        void onClickOk(BaseOverlayDialog dialog);
    }
}
