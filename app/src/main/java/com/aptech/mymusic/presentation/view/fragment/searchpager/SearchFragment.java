package com.aptech.mymusic.presentation.view.fragment.searchpager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.AdsModel;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.domain.entity.TopicModel;
import com.aptech.mymusic.presentation.presenter.Callback;
import com.aptech.mymusic.presentation.presenter.HomePresenter;
import com.aptech.mymusic.presentation.view.activity.MainActivity;
import com.aptech.mymusic.presentation.view.activity.PlayMusicActivity;
import com.aptech.mymusic.presentation.view.adapter.SongAdapter;
import com.aptech.mymusic.presentation.view.adapter.TopicAdapter;
import com.aptech.mymusic.presentation.view.fragment.musicplayer.MainPagerFragment;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.aptech.mymusic.utils.BarsUtils;
import com.google.android.material.chip.ChipGroup;
import com.mct.components.baseui.BaseFragment;

import java.util.List;

public class SearchFragment extends BaseFragment implements View.OnClickListener, SongAdapter.ItemClickedListener, Callback.GetDataSongSearchCallBack {
    private View view;
    private ChipGroup chipGroupRecommend;
    private RecyclerView rcvSearchSong;
    private EditText edtTbSearch;
    private Toolbar toolbarSearch;
    private HomePresenter mHomePresenter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setShowControlLayout(false);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomePresenter = new HomePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_song, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAction();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initAction() {
        toolbarSearch.setNavigationOnClickListener(v -> popLastFragment());
        showSoftInput(edtTbSearch);
        edtTbSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mHomePresenter.getDataSongSearch(s.toString(), SearchFragment.this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView(View view) {
        chipGroupRecommend = view.findViewById(R.id.chip_group_recommend);
        rcvSearchSong = view.findViewById(R.id.rcv_search_song);
        toolbarSearch = view.findViewById(R.id.toolbar_search);
        edtTbSearch = view.findViewById(R.id.edt_tb_search);
    }

    private void setDataTopic(List<SongModel> data) {
        SongAdapter adapter = new SongAdapter(data, this, SongAdapter.TYPE_SEARCH);
        rcvSearchSong.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvSearchSong.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHomePresenter.release();
        mHomePresenter = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setShowControlLayout(true);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onClickedItem(List<SongModel> songs, SongModel song, int position) {
        PlayMusicActivity.start(requireContext(), song);
    }

    @Override
    public void onClickedAdd(SongModel song, int position) {

    }

    @Override
    public void getDataSongSearchSuccess(List<SongModel> data) {
        setDataTopic(data);
    }

    @Override
    public void getDataSongSearchFailure(String error) {

    }

    @Override
    public void showLoading() {
        super.showLoading();

    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }
}