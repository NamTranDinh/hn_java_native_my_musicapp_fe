package com.aptech.mymusic.presentation.view.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aptech.mymusic.R;
import com.aptech.mymusic.di.DataInjection;
import com.aptech.mymusic.domain.entity.AdsModel;
import com.aptech.mymusic.presentation.view.fragment.MainFragment;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.utils.ToastUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    public static int HEIGHT_DEVICE;
    public static int WIDTH_DEVICE;
    public static final int TOTAL_ITEM_CARD = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new MainFragment());
        initView();
    }

    private void initView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        HEIGHT_DEVICE = metrics.heightPixels;
        WIDTH_DEVICE = metrics.widthPixels;
    }


    @Override
    protected int getContainerId() {
        return R.id.main_frame;
    }

    @Override
    protected void showToastOnBackPressed() {
        Toast.makeText(this, "Back again to exits!", Toast.LENGTH_SHORT).show();
    }
}