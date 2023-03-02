package com.aptech.mymusic.domain.repository;

import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.domain.entity.CategoryModel;

import java.util.List;

import retrofit2.Call;

public interface AlbumRepository {

    Call<List<AlbumModel>> getRandAlbum();

    Call<List<AlbumModel>> getAllAlbum();

}
