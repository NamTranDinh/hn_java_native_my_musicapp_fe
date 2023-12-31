package com.aptech.mymusic.presentation.view.fragment.mainpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.TopicModel;
import com.aptech.mymusic.presentation.presenter.Callback;
import com.aptech.mymusic.presentation.presenter.TopicPresenter;
import com.aptech.mymusic.presentation.view.adapter.TopicAdapter;

import java.util.List;

public class TopicTabFragment extends BaseTabFragment implements Callback.GetDataTopicCallBack {

    protected LottieAnimationView mImgLoading;
    private RecyclerView rcvCard;
    private TopicPresenter topicPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topicPresenter = new TopicPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_topic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImgLoading = view.findViewById(R.id.icon_loading);
        rcvCard = view.findViewById(R.id.rcv_card);

        showLoading(mImgLoading);
        topicPresenter.getDataTopic(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topicPresenter.release();
        topicPresenter = null;
    }

    private void setDataTopic(List<TopicModel> data) {
        hideLoading(mImgLoading);
        rcvCard.setAdapter(new TopicAdapter(data, this));
        rcvCard.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void getDataTopicSuccess(List<TopicModel> data) {
        setDataTopic(data);
    }

    @Override
    public void getDataTopicFailure(String error) {

    }

    @NonNull
    @Override
    protected RecyclerView getScrollView() {
        return rcvCard;
    }
}
