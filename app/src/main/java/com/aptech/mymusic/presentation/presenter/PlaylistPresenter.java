package com.aptech.mymusic.presentation.presenter;

import androidx.annotation.NonNull;

import com.aptech.mymusic.di.DataInjection;
import com.aptech.mymusic.domain.entity.PlaylistModel;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PlaylistPresenter extends BasePresenter {
    public PlaylistPresenter(BaseView baseView) {
        super(baseView);
    }

    public void getDataAllPlaylist(Callback.GetDataPlayListCallBack callBack) {
        DataInjection.provideRepository().getPlaylistRepository().getAllPlayList().enqueue(new retrofit2.Callback<List<PlaylistModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<PlaylistModel>> call, @NonNull Response<List<PlaylistModel>> response) {
                if (response.body() != null) {
                    callBack.getDataPlaylistSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PlaylistModel>> call, @NonNull Throwable t) {
                callBack.getDataPlaylistFailure(t.getMessage());
            }
        });
    }
}
