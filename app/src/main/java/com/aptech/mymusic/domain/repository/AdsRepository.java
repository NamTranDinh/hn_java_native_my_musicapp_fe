package com.aptech.mymusic.domain.repository;

import com.aptech.mymusic.domain.entity.AdsModel;

import java.util.List;

import retrofit2.Call;

public interface AdsRepository {

    Call<List<AdsModel>> getDataBanner();

}
