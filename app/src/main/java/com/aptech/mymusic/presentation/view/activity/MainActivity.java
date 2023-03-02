package com.aptech.mymusic.presentation.view.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.aptech.mymusic.R;
import com.aptech.mymusic.presentation.view.fragment.MainFragment;
import com.mct.components.baseui.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new MainFragment());
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