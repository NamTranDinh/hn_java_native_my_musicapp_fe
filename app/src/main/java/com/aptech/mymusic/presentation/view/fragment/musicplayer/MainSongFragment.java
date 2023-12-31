package com.aptech.mymusic.presentation.view.fragment.musicplayer;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_VIEW;

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
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.service.MusicDelegate;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.aptech.mymusic.utils.GlideUtils;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.utils.ToastUtils;

public class MainSongFragment extends BaseFragment {

    private CardView cvThumb;
    private ImageView imgSong;
    private ImageView imgLike;
    private Integer mSongId;


    BroadcastReceiver receiverFromMusicService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            if (MusicServiceHelper.getCurrentSong() == null) {
                return;
            }
            initData();
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
        cvThumb = view.findViewById(R.id.cv_thumb);
        cvThumb.post(() -> cvThumb.setRadius(cvThumb.getMeasuredWidth() / 2f));
        imgSong = view.findViewById(R.id.img_song);
        imgLike = view.findViewById(R.id.img_likes);
        imgLike.setOnClickListener(v -> {
            SongModel songCurr = MusicServiceHelper.getCurrentSong();
            if (songCurr != null) {
                boolean isSongFavor = MusicServiceHelper.getMusicPreference().toggleFavorSong(songCurr);
                if (isSongFavor) {
                    imgLike.setImageResource(R.drawable.ic_heart_fill);
                    showToast("Added to favorite", ToastUtils.INFO, true);
                } else {
                    imgLike.setImageResource(R.drawable.ic_heart);
                    showToast("Removed from favorite", ToastUtils.INFO, true);
                }
                MusicServiceHelper.sendAction(MusicDelegate.Action.UPDATE_VIEW);
            }
        });
        view.findViewById(R.id.tv_playlist).setOnClickListener(v ->
                replaceFragment(new PlaylistSongFragment(), true, BaseActivity.Anim.TRANSIT_FADE)
        );
        initData();
    }

    private void initData() {
        SongModel song = MusicServiceHelper.getCurrentSong();
        boolean isPlaying = MusicServiceHelper.isPlaying();
        if (!song.getId().equals(mSongId)) {
            mSongId = song.getId();
            imgSong.setRotation(0);
        }
        boolean isSongFavor = MusicServiceHelper.getMusicPreference().isFavorite(song);
        if (isSongFavor) {
            imgLike.setImageResource(R.drawable.ic_heart_fill);
        } else {
            imgLike.setImageResource(R.drawable.ic_heart);
        }
        GlideUtils.load(song.getImageUrl(), imgSong);
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
