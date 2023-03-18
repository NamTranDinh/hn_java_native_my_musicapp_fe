package com.aptech.mymusic.presentation.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.aptech.mymusic.R;
import com.mct.components.baseui.BaseOverlayDialog;

public class TimerDialog extends BaseOverlayDialog {

    private final int hour;
    private final int minute;
    private final boolean is24Hour;
    private final IOnClickListener onClickListener;
    private TimePicker timePicker;

    public TimerDialog(@NonNull Context context, IOnClickListener onClickListener) {
        this(context, 0, 0, onClickListener);
    }

    public TimerDialog(@NonNull Context context, int hour, int minute, IOnClickListener onClickListener) {
        this(context, hour, minute, true, onClickListener);
    }

    public TimerDialog(@NonNull Context context, int hour, int minute, boolean is24Hour, IOnClickListener onClickListener) {
        super(context);
        this.hour = hour;
        this.minute = minute;
        this.is24Hour = is24Hour;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_timer, null);
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
        timePicker = view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(is24Hour);
        timePicker.setHour(hour);
        timePicker.setMinute(minute);

        view.findViewById(R.id.dialog_cancel_button).setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onCancel(this);
            } else {
                dismiss();
            }
        });
        view.findViewById(R.id.dialog_ok_button).setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onTimeSet(this, timePicker.getHour(), timePicker.getMinute());
            } else {
                dismiss();
            }
        });
    }

    public interface IOnClickListener {
        void onCancel(BaseOverlayDialog dialog);

        void onTimeSet(BaseOverlayDialog dialog, int hour, int minute);
    }
}
