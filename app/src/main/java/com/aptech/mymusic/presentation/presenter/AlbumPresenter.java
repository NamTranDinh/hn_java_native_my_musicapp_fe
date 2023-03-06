package com.aptech.mymusic.presentation.presenter;

import androidx.annotation.NonNull;

import com.aptech.mymusic.di.DataInjection;
import com.aptech.mymusic.domain.entity.AlbumModel;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AlbumPresenter extends BasePresenter {
    public AlbumPresenter(BaseView baseView) {
        super(baseView);
    }

    public void getDataAlbum(Callback.GetDataAlbumCallBack callBack) {
        DataInjection.provideRepository().getAlbumRepository().getAllAlbum().enqueue(new retrofit2.Callback<List<AlbumModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<AlbumModel>> call, @NonNull Response<List<AlbumModel>> response) {
                if (response.body() != null) {
                    callBack.getDataAlbumSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AlbumModel>> call, @NonNull Throwable t) {
                callBack.getDataAlbumFailure(t.getMessage());
            }
        });
    }
}
