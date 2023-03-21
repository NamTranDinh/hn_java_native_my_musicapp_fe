package com.aptech.mymusic.presentation.presenter;

import androidx.annotation.NonNull;

import com.aptech.mymusic.di.DataInjection;
import com.aptech.mymusic.domain.entity.AdsModel;
import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.domain.entity.CategoryModel;
import com.aptech.mymusic.domain.entity.PlaylistModel;
import com.aptech.mymusic.domain.entity.SongModel;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HomePresenter extends BasePresenter {

    public HomePresenter(BaseView baseView) {
        super(baseView);
    }

    /* Banner*/
    public void getDataBanner(Callback.GetDataBannerCallBack callBack) {
        DataInjection.provideRepository().getAdsRepository().getDataBanner().enqueue(new retrofit2.Callback<List<AdsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<AdsModel>> call, @NonNull Response<List<AdsModel>> response) {
                if (response.body() != null) {
                    callBack.getDataBannerSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AdsModel>> call, @NonNull Throwable t) {
                callBack.getDataBannerFailure(t.getMessage());
            }
        });
    }

    public void getDataPlaylistTop100(Callback.GetDataPlayListCallBack callBack) {
        DataInjection.provideRepository().getPlaylistRepository().getRandPlaylist().enqueue(new retrofit2.Callback<List<PlaylistModel>>() {
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

    public void getDataAlbum(Callback.GetDataAlbumCallBack callBack) {
        DataInjection.provideRepository().getAlbumRepository().getRandAlbum().enqueue(new retrofit2.Callback<List<AlbumModel>>() {
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

    public void getDataCategoryFavor(Callback.GetDataCategoryFavorCallBack callBack) {
        DataInjection.provideRepository().getCategoryRepository().getRandCategory().enqueue(new retrofit2.Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryModel>> call, @NonNull Response<List<CategoryModel>> response) {
                if (response.body() != null) {
                    callBack.getDataCateFavorSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryModel>> call, @NonNull Throwable t) {
                callBack.getDataCateFavorFailure(t.getMessage());
            }
        });
    }

    public void getDataAllCategory(int idTopic, Callback.GetDataAllCategoryCallBack callBack) {
        DataInjection.provideRepository().getCategoryRepository().getAllCategoryInTopic(idTopic).enqueue(new retrofit2.Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryModel>> call, @NonNull Response<List<CategoryModel>> response) {
                if (response.body() != null) {
                    callBack.getDataAllCategorySuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryModel>> call, @NonNull Throwable t) {
                callBack.getDataAllCategoryFailure(t.getMessage());
            }
        });
    }

    public void getDataAllSong(String type, long id, Callback.GetDataAllSongCallBack callback) {
        DataInjection.provideRepository().getSongRepository().getAllSongFrom(type, id).enqueue(new retrofit2.Callback<List<SongModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<SongModel>> call, @NonNull Response<List<SongModel>> response) {
                callback.getDataSongSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<SongModel>> call, @NonNull Throwable t) {
                callback.getDataSongFailure(t.getMessage());
            }
        });
    }

    public void getDataSongSearch(String nameSong, Callback.GetDataSongSearchCallBack callback) {
        DataInjection.provideRepository().getSongRepository().searchSongByName(nameSong).enqueue(new retrofit2.Callback<List<SongModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<SongModel>> call, @NonNull Response<List<SongModel>> response) {
                getBaseView().showLoading();
                if (response.body() != null) {
                    callback.getDataSongSearchSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SongModel>> call, @NonNull Throwable t) {
                getBaseView().hideLoading();
                callback.getDataSongSearchFailure(t.getMessage());
            }
        });
    }

    public void getDataNewReleasedMusic(Callback.GetDataNewReleaseMusicCallBack callBack) {
        DataInjection.provideRepository().getSongRepository().getNewlyReleasedMusic().enqueue(new retrofit2.Callback<List<SongModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<SongModel>> call, @NonNull Response<List<SongModel>> response) {
                if (response.body() != null) {
                    callBack.getDataReleaseMusicSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SongModel>> call, @NonNull Throwable t) {
                callBack.getDataReleaseMusicFailure(t.getMessage());
            }
        });
    }


}