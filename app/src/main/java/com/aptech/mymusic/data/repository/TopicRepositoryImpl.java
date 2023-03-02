package com.aptech.mymusic.data.repository;

import com.aptech.mymusic.data.sever.DataService;
import com.aptech.mymusic.domain.entity.TopicModel;
import com.aptech.mymusic.domain.repository.TopicRepository;

import java.util.List;

import retrofit2.Call;

public class TopicRepositoryImpl implements TopicRepository {

    @Override
    public Call<List<TopicModel>> getAllTopic() {
        return DataService.getInstance().getAllTopic();
    }
}
