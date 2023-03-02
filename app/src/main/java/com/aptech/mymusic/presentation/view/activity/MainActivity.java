package com.aptech.mymusic.presentation.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aptech.mymusic.R;
import com.aptech.mymusic.di.DataInjection;
import com.aptech.mymusic.domain.entity.AdsModel;
import com.aptech.mymusic.presentation.view.fragment.MainFragment;
import com.mct.components.baseui.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new MainFragment());

        DataInjection.provideRepository().getAdsRepository().getDataBanner().enqueue(new Callback<List<AdsModel>>() {
            @Override
            public void onResponse(Call<List<AdsModel>> call, Response<List<AdsModel>> response) {
                Log.d("namtd", "onResponse:  " + response.body() + "");
            }

            @Override
            public void onFailure(Call<List<AdsModel>> call, Throwable t) {
                Log.d("namtd", "onResponse:  " + t.getMessage() + "");

            }
        });
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