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
import com.mct.components.baseui.BaseFragment;

import java.util.List;

public class TopicFragment extends BaseFragment implements Callback.GetDataTopicCallBack {
    private View view;
    private RecyclerView rcvTopic;
    private TopicPresenter topicPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_topic, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rcvTopic = view.findViewById(R.id.rcv_topic);
        topicPresenter = new TopicPresenter(this);
        topicPresenter.getDataTopic(this);
    }

    private void setAdapter(List<TopicModel> data) {
        TopicAdapter adapter = new TopicAdapter(getContext(), data);
        rcvTopic.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getContext(), MainActivity.ONE_ITEM_CARD);
        rcvTopic.setLayoutManager(manager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void getDataTopicSuccess(List<TopicModel> data) {
        setAdapter(data);
    }

    @Override
    public void getDataTopicFailure(String error) {

    }
}
