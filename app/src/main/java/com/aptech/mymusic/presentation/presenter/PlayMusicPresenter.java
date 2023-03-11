package com.aptech.mymusic.presentation.presenter;

import androidx.annotation.NonNull;

import com.aptech.mymusic.di.DataInjection;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.utils.JsonHelper;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Response;

public class PlayMusicPresenter extends BasePresenter {

    public PlayMusicPresenter(BaseView baseView) {
        super(baseView);
    }

    public void requestSuggestSong(@NonNull SongModel song, @NonNull List<SongModel> blacklist, Integer limit,
                                   @NonNull Callback.GetSuggestSongCallback callback) {
        String blacklistIds = JsonHelper.objToJson(blacklist.stream().map(SongModel::getId).collect(Collectors.toList()));
        DataInjection.provideRepository().getSongRepository()
                .getSuggestSong(Long.valueOf(song.getId()), blacklistIds, limit)
                .enqueue(new retrofit2.Callback<List<SongModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<SongModel>> call, @NonNull Response<List<SongModel>> response) {
                        if (response.isSuccessful()) {
                            callback.onGot(response.body());
                        } else {
                            callback.onGot(Collections.emptyList());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<SongModel>> call, @NonNull Throwable t) {
                        getBaseView().onFalse(t);
                    }
                });
    }
}
