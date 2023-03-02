package com.aptech.mymusic.data.repository;

import com.aptech.mymusic.data.sever.DataService;
import com.aptech.mymusic.domain.entity.PlaylistModel;
import com.aptech.mymusic.domain.repository.PlaylistRepository;

import java.util.List;

import retrofit2.Call;

public class PlaylistRepositoryImpl implements PlaylistRepository {

    @Override
    public Call<List<PlaylistModel>> getRandPlaylist() {
        return DataService.getInstance().getRandPlaylist();
    }

    @Override
    public Call<List<PlaylistModel>> getAllPlayList() {
        return DataService.getInstance().getAllPlayList();
    }
}
