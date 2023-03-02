package com.aptech.mymusic.data.repository;

import com.aptech.mymusic.data.sever.DataService;
import com.aptech.mymusic.domain.entity.AdsModel;
import com.aptech.mymusic.domain.repository.AdsRepository;

import java.util.List;

import retrofit2.Call;

public class AdsRepositoryImpl implements AdsRepository {

    @Override
    public Call<List<AdsModel>> getDataBanner() {
        return DataService.getInstance().getDataBanner();
    }
}
