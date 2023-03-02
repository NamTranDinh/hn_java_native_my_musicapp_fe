package com.aptech.mymusic.domain.repository;

import com.aptech.mymusic.domain.entity.PlaylistModel;

import java.util.List;

import retrofit2.Call;

public interface PlaylistRepository {

    Call<List<PlaylistModel>> getRandPlaylist();

    Call<List<PlaylistModel>> getAllPlayList();

}
