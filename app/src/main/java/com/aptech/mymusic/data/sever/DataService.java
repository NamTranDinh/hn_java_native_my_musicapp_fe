package com.aptech.mymusic.data.sever;

import androidx.annotation.NonNull;

import com.aptech.mymusic.config.ServerConfig;
import com.aptech.mymusic.domain.entity.AdsModel;
import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.domain.entity.CategoryModel;
import com.aptech.mymusic.domain.entity.PlaylistModel;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.domain.entity.TopicModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DataService {

    static DataService getInstance() {
        return Holder.INSTANCE;
    }

    class Holder {

        private static final DataService INSTANCE = getClient().create(DataService.class);

        @NonNull
        private static Retrofit getClient() {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(10000, TimeUnit.MILLISECONDS)
                    .writeTimeout(10000, TimeUnit.MILLISECONDS)
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .build();
            Gson gson = new GsonBuilder().setLenient().create();

            return new Retrofit.Builder()
                    .baseUrl(ServerConfig.BASE_API)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

    }

    @GET("song_banner")
    Call<List<AdsModel>> getDataBanner();

    @GET("rand_category_for_current_day")
    Call<List<CategoryModel>> getRandCategory();

    @GET("rand_album_for_current_day")
    Call<List<AlbumModel>> getRandAlbum();

    @GET("rand_playlist_for_current_day")
    Call<List<PlaylistModel>> getRandPlaylist();

    @GET("all_playlist")
    Call<List<PlaylistModel>> getAllPlayList();

    @GET("all_album")
    Call<List<AlbumModel>> getAllAlbum();

    @GET("all_topic")
    Call<List<TopicModel>> getAllTopic();

    @FormUrlEncoded
    @POST("all_category_in_topic")
    Call<List<CategoryModel>> getAllCategoryInTopic(@Field("id") int topicId);

    @GET("newly_released_music")
    Call<List<SongModel>> getNewlyReleasedMusic();

    @GET("all_song_from/{type}/{id}")
    Call<List<SongModel>> getAllSongFrom(@Path("type") String type, @Path("id") Long id);

    @GET("get_suggest_song/{id}/{list_current_song_id}")
    Call<List<SongModel>> getSuggestSong(@Path("id") Long id, @Path("list_current_song_id") String listCurrentSongId);

}
