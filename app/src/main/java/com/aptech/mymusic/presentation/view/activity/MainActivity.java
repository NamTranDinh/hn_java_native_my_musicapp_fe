package com.aptech.mymusic.presentation.view.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.aptech.mymusic.R;
import com.aptech.mymusic.presentation.view.fragment.MainFragment;
import com.mct.components.baseui.BaseActivity;

public class MainActivity extends BaseActivity {
    public static int HEIGHT_DEVICE;
    public static int WIDTH_DEVICE;
    public static final int TWO_ITEM_CARD = 2;
    public static final int ONE_ITEM_CARD = 1;
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