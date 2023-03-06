package com.aptech.mymusic.presentation.presenter;

import com.aptech.mymusic.domain.entity.AdsModel;
import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.domain.entity.CategoryModel;
import com.aptech.mymusic.domain.entity.PlaylistModel;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.domain.entity.TopicModel;

import java.util.List;

public abstract class Callback {

    public interface GetDataBannerCallBack {
        void getDataBannerSuccess(List<AdsModel> data);

        void getDataBannerFailure(String error);
    }

    public interface GetDataAlbumCallBack {
        void getDataAlbumSuccess(List<AlbumModel> data);

        void getDataAlbumFailure(String error);
    }

    public interface GetDataPlayListCallBack {
        void getDataPlaylistSuccess(List<PlaylistModel> data);

        void getDataPlaylistFailure(String error);
    }

    public interface GetDataCategoryFavorCallBack {
        void getDataCateFavorSuccess(List<CategoryModel> data);

        void getDataCateFavorFailure(String error);
    }

    public interface GetDataNewReleaseMusicCallBack {
        void getDataReleaseMusicSuccess(List<SongModel> data);

        void getDataReleaseMusicFailure(String error);
    }
    public interface GetDataTopicCallBack {
        void getDataTopicSuccess(List<TopicModel> data);

        void getDataTopicFailure(String error);
    }

}
