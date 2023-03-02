package com.aptech.mymusic.domain.repository;

import com.aptech.mymusic.domain.entity.CategoryModel;

import java.util.List;

import retrofit2.Call;

public interface CategoryRepository {

    Call<List<CategoryModel>> getRandCategory();

    Call<List<CategoryModel>> getAllCategoryInTopic(int topicId);
}
