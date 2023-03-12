package com.aptech.mymusic.presentation.view.fragment.musicplayer;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_VIEW;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.aptech.mymusic.utils.GlideUtils;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;

public class MainSongFragment extends BaseFragment {

    private ImageView imgSong;
    private Integer mSongId;

    BroadcastReceiver receiverFromMusicService = new BroadcastReceiver() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                SongModel song = (SongModel) bundle.getSerializable(KEY_CURRENT_SONG);
                boolean isPlaying = bundle.getBoolean(KEY_IS_PLAYING);
                initData(song, isPlaying);
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiverFromMusicService, new IntentFilter(ACTION_UPDATE_VIEW));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_music_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgSong = view.findViewById(R.id.img_song);
        view.findViewById(R.id.img_likes).setOnClickListener(v -> {

        });
        view.findViewById(R.id.tv_playlist).setOnClickListener(v ->
                replaceFragment(new PlaylistSongFragment(), true, BaseActivity.Anim.TRANSIT_FADE)
        );
        initData(MusicServiceHelper.getCurrentSong(), MusicServiceHelper.isPlaying());
    }

    private void initData(SongModel song, boolean isPlaying) {
        if (song != null) {
            if (!song.getId().equals(mSongId)) {
                mSongId = song.getId();
                imgSong.setRotation(0);
            }
            GlideUtils.load(song.getImageUrl(), imgSong);
        }
        if (isPlaying) {
            startAnim();
        } else {
            stopAnim();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiverFromMusicService);
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
