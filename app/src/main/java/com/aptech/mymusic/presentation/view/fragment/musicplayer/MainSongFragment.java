package com.aptech.mymusic.presentation.view.fragment.musicplayer;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_VIEW;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.UPDATE_VIEW;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_CURRENT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_IS_PLAYING;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.service.MusicService;
import com.aptech.mymusic.presentation.view.service.MusicStarter;
import com.bumptech.glide.Glide;

public class MainSongFragment extends Fragment {

    private View view;
    private ImageView imgSong, imgLikes;
    private LinearLayout llPlaylist;

    BroadcastReceiver receiverFromMusicService = new BroadcastReceiver() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                SongModel song = (SongModel) bundle.getSerializable(KEY_CURRENT_SONG);
                boolean isPlaying = bundle.getBoolean(KEY_IS_PLAYING);
                Glide.with(context).load(song.getImageUrl()).into(imgSong);
                if (isPlaying) {
                    startAnim();
                } else {
                    stopAnim();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_song, container, false);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiverFromMusicService, new IntentFilter(ACTION_UPDATE_VIEW));
        if (MusicService.isPlaying()) {
            MusicStarter.startService(UPDATE_VIEW);
        }
        initUi();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiverFromMusicService);
    }

    private void initUi() {
        imgSong = view.findViewById(R.id.img_song);
        imgLikes = view.findViewById(R.id.img_likes);
        llPlaylist = view.findViewById(R.id.ll_playlist);
//        llPlaylist.setOnClickListener(view -> ((PlayMusicActivity) requireActivity()).showPlaylistFragment());
    }

    private void startAnim() {
        stopAnim();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                imgSong.animate().rotationBy(360)
                        .withEndAction(this)
                        .setDuration(10000)
                        .setInterpolator(new LinearInterpolator())
                        .start();
            }
        };
        imgSong.animate().rotationBy(360)
                .withEndAction(runnable)
                .setDuration(10000)
                .setInterpolator(new LinearInterpolator())
                .start();
    }

    private void stopAnim() {
        imgSong.animate().cancel();
    }
}
