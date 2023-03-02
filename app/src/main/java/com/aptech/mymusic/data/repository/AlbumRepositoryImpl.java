package com.aptech.mymusic.data.repository;

import com.aptech.mymusic.data.sever.DataService;
import com.aptech.mymusic.domain.entity.AdsModel;
import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.domain.repository.AdsRepository;
import com.aptech.mymusic.domain.repository.AlbumRepository;

import java.util.List;

import retrofit2.Call;

public class AlbumRepositoryImpl implements AlbumRepository {


    @Override
    public Call<List<AlbumModel>> getRandAlbum() {
        return DataService.getInstance().getRandAlbum();
    }

    @Override
    public Call<List<AlbumModel>> getAllAlbum() {
        return DataService.getInstance().getAllAlbum();
    }
}
