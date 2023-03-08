package com.aptech.mymusic.presentation.view.fragment.musicplayer;


import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_VIEW;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_CURRENT_SONG;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.activity.PlayMusicActivity;
import com.google.gson.Gson;

public class DetailSongFragment extends Fragment {

    private RecyclerView rcvListSuggestSong;
    private SongModel mSong;

    BroadcastReceiver receiverFromMusicService = new BroadcastReceiver() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mSong = (SongModel) bundle.getSerializable(KEY_CURRENT_SONG);
                initData(mSong);
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggest_song, container, false);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiverFromMusicService, new IntentFilter(ACTION_UPDATE_VIEW));
        rcvListSuggestSong = view.findViewById(R.id.rcv_list_suggest_song);

        initData(mSong);

        return view;
    }

    public void initData(SongModel song) {
        Gson gson = new Gson();
        String songJson = gson.toJson(song);
//        DataService dataService = APIService.getService();
//        Call<List<SongModel>> callback = dataService.getSuggestSong(songJson, MusicService.getListSongId());
//        callback.enqueue(new Callback<List<SongModel>>() {
//            @Override
//            public void onResponse(@NonNull Call<List<SongModel>> call, @NonNull Response<List<SongModel>> response) {
//                if(response.body() != null){
//                    SongAdapter adapter = new SongAdapter(getContext(), new ArrayList<>(response.body()), SongAdapter.TYPE_SUGGEST);
//                    rcvListSuggestSong.setAdapter(adapter);
//
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//                    rcvListSuggestSong.setLayoutManager(layoutManager);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<SongModel>> call, @NonNull Throwable t) {
//                Log.e("ddd", "onFailure: ", t);
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiverFromMusicService);
    }
}
