package com.aptech.mymusic.presentation.view.fragment.homepages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.adapter.SongAdapter;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.mct.components.baseui.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FavouriteFragment extends BaseFragment implements SongAdapter.ItemClickedListener {

    private Toolbar tbFavorSong;
    private RecyclerView rcvListSongFavor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        tbFavorSong = view.findViewById(R.id.tb_favor_song);
        rcvListSongFavor = view.findViewById(R.id.rcv_list_song_favor);
        Set<SongModel> listSong = MusicServiceHelper.getMusicPreference().getFavoriteSong();
        SongAdapter adapter = new SongAdapter(new ArrayList<>(listSong), this, SongAdapter.TYPE_FAVORITE);
        rcvListSongFavor.setAdapter(adapter);
        rcvListSongFavor.setLayoutManager(new LinearLayoutManager(requireContext()));
        tbFavorSong.setNavigationOnClickListener(v -> popLastFragment());
    }

    @Override
    public void onClickedItem(List<SongModel> songs, SongModel song, int position) {

    }

    @Override
    public void onClickedAdd(SongModel song, int position) {

    }
}
