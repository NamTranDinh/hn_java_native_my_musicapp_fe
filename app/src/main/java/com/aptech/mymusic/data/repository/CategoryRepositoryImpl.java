package com.aptech.mymusic.data.repository;

import com.aptech.mymusic.data.sever.DataService;
import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.domain.entity.CategoryModel;
import com.aptech.mymusic.domain.repository.AlbumRepository;
import com.aptech.mymusic.domain.repository.CategoryRepository;

import java.util.List;

import retrofit2.Call;

public class CategoryRepositoryImpl implements CategoryRepository {

    @Override
    public Call<List<CategoryModel>> getRandCategory() {
        return DataService.getInstance().getRandCategory();
    }

    @Override
    public Call<List<CategoryModel>> getAllCategoryInTopic(int topicId) {
        return DataService.getInstance().getAllCategoryInTopic(topicId);
    }
}
