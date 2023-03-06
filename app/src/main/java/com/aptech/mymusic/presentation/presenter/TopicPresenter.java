package com.aptech.mymusic.presentation.presenter;

import androidx.annotation.NonNull;

import com.aptech.mymusic.di.DataInjection;
import com.aptech.mymusic.domain.entity.TopicModel;
import com.mct.components.baseui.BasePresenter;
import com.mct.components.baseui.BaseView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class TopicPresenter extends BasePresenter {
    public TopicPresenter(BaseView baseView) {
        super(baseView);
    }

    public void getDataTopic(Callback.GetDataTopicCallBack callback) {
        DataInjection.provideRepository().getTopicRepository().getAllTopic().enqueue(new retrofit2.Callback<List<TopicModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<TopicModel>> call, @NonNull Response<List<TopicModel>> response) {
                callback.getDataTopicSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<TopicModel>> call, @NonNull Throwable t) {
                callback.getDataTopicFailure(t.getMessage());
            }
        });
    }
}
