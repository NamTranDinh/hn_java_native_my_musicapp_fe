package com.aptech.mymusic.presentation.view.fragment.mainpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.TopicModel;
import com.aptech.mymusic.presentation.presenter.Callback;
import com.aptech.mymusic.presentation.presenter.TopicPresenter;
import com.aptech.mymusic.presentation.view.activity.MainActivity;
import com.aptech.mymusic.presentation.view.adapter.TopicAdapter;

import java.util.List;

public class TopicFragment extends BaseTabFragment implements Callback.GetDataTopicCallBack {
    private RecyclerView rcvTopic;
    private TopicPresenter topicPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topicPresenter = new TopicPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rcvTopic = view.findViewById(R.id.rcv_topic);
        topicPresenter.getDataTopic(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topicPresenter.release();
        topicPresenter = null;
    }

    private void setAdapter(List<TopicModel> data) {
        TopicAdapter adapter = new TopicAdapter(getContext(), data, this);
        rcvTopic.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getContext(), MainActivity.ONE_ITEM_CARD);
        rcvTopic.setLayoutManager(manager);
    }

    @Override
    public void getDataTopicSuccess(List<TopicModel> data) {
        setAdapter(data);
    }

    @Override
    public void getDataTopicFailure(String error) {

    }

    @NonNull
    @Override
    protected RecyclerView getScrollView() {
        return rcvTopic;
    }
}
