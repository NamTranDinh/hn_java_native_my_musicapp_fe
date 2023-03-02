package com.aptech.mymusic.domain.repository;

import com.aptech.mymusic.domain.entity.TopicModel;

import java.util.List;

import retrofit2.Call;

public interface TopicRepository {

    Call<List<TopicModel>> getAllTopic();

}
