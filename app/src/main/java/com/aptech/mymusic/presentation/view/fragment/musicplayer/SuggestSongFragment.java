package com.aptech.mymusic.presentation.view.fragment.musicplayer;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_VIEW;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.presenter.Callback;
import com.aptech.mymusic.presentation.presenter.PlayMusicPresenter;
import com.aptech.mymusic.presentation.view.adapter.SongAdapter;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.utils.ToastUtils;

import java.util.List;
import java.util.Random;

public class SuggestSongFragment extends BaseFragment implements Callback.GetSuggestSongCallback, SongAdapter.ItemClickedListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRcvListSuggestSong;
    private PlayMusicPresenter mPresenter;
    private Integer mSongId;

    BroadcastReceiver receiverFromMusicService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            SongModel song = MusicServiceHelper.getCurrentSong();
            if (song != null && !song.getId().equals(mSongId)) {
                mSongId = song.getId();
                initData();
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mPresenter = new PlayMusicPresenter(this);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiverFromMusicService, new IntentFilter(ACTION_UPDATE_VIEW));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_music_suggest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = view.findViewById(R.id.srl_refresh);
        mRcvListSuggestSong = view.findViewById(R.id.rcv_list_suggest_song);
        mRcvListSuggestSong.setLayoutManager(new LinearLayoutManager(requireContext()));
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.postDelayed(this::initData, 600));
        initData();
    }

    public void initData() {
        SongModel song = MusicServiceHelper.getCurrentSong();
        List<SongModel> songs = MusicServiceHelper.getCurrentListSong();
        mPresenter.requestSuggestSong(song, songs, new Random().nextInt(8) + 3, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiverFromMusicService);
        mPresenter.release();
        mPresenter = null;
    }

    @Override
    public void onGot(List<SongModel> data) {
        mSwipeRefreshLayout.setRefreshing(false);
        mRcvListSuggestSong.setAdapter(new SongAdapter(data, this, SongAdapter.TYPE_SUGGEST));
    }

    @Override
    public void onFalse(Throwable t) {
        Log.e("ddd", "onError: ", t);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClickedItem(List<SongModel> songs, SongModel song, int position) {
        MusicServiceHelper.playSong(song);
        if (getParentFragment() instanceof MainPagerFragment) {
            ((MainPagerFragment) getParentFragment()).jumpToPage(1, true);
        }
    }

    @Override
    public void onClickedAdd(SongModel song, int position) {
        MusicServiceHelper.addSong(song);
        showToast(String.format("Added %s to playlist", song.getName()), ToastUtils.INFO, true);
    }
}
