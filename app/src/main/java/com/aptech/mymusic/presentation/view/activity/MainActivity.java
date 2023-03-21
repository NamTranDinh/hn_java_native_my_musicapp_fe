package com.aptech.mymusic.presentation.view.activity;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.NEXT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PAUSE_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_SONG;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.aptech.mymusic.R;
import com.aptech.mymusic.config.MusicConfig;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.fragment.MainFragment;
import com.aptech.mymusic.presentation.view.service.MusicDelegate;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.aptech.mymusic.utils.AnimateUtils;
import com.aptech.mymusic.utils.GlideUtils;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.utils.ScreenUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final int TWO_ITEM_CARD = 2;

    private ViewGroup controlLayout;
    private SeekBar seekBar;
    private ImageView imgThumb;
    private TextView tvSongName;
    private TextView tvSingerName;
    private ImageView imgLikes;
    private LottieAnimationView imgPlayPause;
    private Runnable seekBarUpdateRunnable;

    BroadcastReceiver receiverFromMusicService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            initData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverFromMusicService, new IntentFilter(MusicDelegate.ACTION_UPDATE_VIEW));
        setContentView(R.layout.activity_main);
        replaceFragment(new MainFragment());
        initUi();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        seekBar.removeCallbacks(seekBarUpdateRunnable);
        seekBar.post(seekBarUpdateRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        seekBar.removeCallbacks(seekBarUpdateRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverFromMusicService);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUi() {
        controlLayout = findViewById(R.id.control_layout);
        seekBar = findViewById(R.id.sb_music);
        seekBar.setOnTouchListener((v, event) -> true);

        imgThumb = findViewById(R.id.img_thumb);
        tvSongName = findViewById(R.id.tv_song_name);
        tvSingerName = findViewById(R.id.tv_singer_name);
        imgLikes = findViewById(R.id.img_likes);
        imgPlayPause = findViewById(R.id.img_play_pause);

        controlLayout.setOnClickListener(this);
        imgLikes.setOnClickListener(this);
        imgPlayPause.setOnClickListener(this);
        findViewById(R.id.img_next).setOnClickListener(this);

    }

    private void initData() {
        SongModel song = MusicServiceHelper.getCurrentSong();
        boolean isPlaying = MusicServiceHelper.isPlaying();
        if (isPlaying) {
            startAnim();
        } else {
            stopAnim();
        }
        initPlayPauseBtn(isPlaying);
        if (song != null) {
            tvSongName.setText(song.getName());
            tvSingerName.setText(song.getSingerName());
            tvSingerName.setVisibility(View.VISIBLE);
            GlideUtils.load(song.getImageUrl(), imgThumb);
            initSeekbar();
            setShowControlLayout(true);
        } else {
            setShowControlLayout(false);
        }
    }

    private void initSeekbar() {
        seekBar.setMax(MusicServiceHelper.getDuration());
        seekBar.setProgress(MusicServiceHelper.getCurrentPosition());
        if (seekBarUpdateRunnable == null) {
            seekBarUpdateRunnable = () -> {
                if (MusicServiceHelper.isPlaying()) {
                    seekBar.setProgress(MusicServiceHelper.getCurrentPosition());
                    seekBar.postDelayed(seekBarUpdateRunnable, 500);
                }
            };
        }
        seekBar.removeCallbacks(seekBarUpdateRunnable);
        seekBar.post(seekBarUpdateRunnable);
    }

    private void initPlayPauseBtn(boolean isPlay) {
        float minProgress, maxProgress;
        double progress = Math.floor(imgPlayPause.getProgress() * 10) / 10;
        if (isPlay) {
            minProgress = 0f;
            maxProgress = 0.5f;
        } else {
            minProgress = 0.5f;
            maxProgress = 1f;
        }
        if (progress >= minProgress && progress <= maxProgress) {
            // If the progress is already within the desired range,
            // continue animating from current progress
            imgPlayPause.setMinAndMaxProgress((float) progress, maxProgress);
        } else {
            // If the progress is outside of the desired range,
            // animate from the start of the range
            imgPlayPause.setProgress(minProgress);
            imgPlayPause.setMinAndMaxProgress(minProgress, maxProgress);
        }
        imgPlayPause.playAnimation();
    }

    private void startAnim() {
        stopAnim();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                imgThumb.animate().rotationBy(360)
                        .withEndAction(this)
                        .setDuration(10000)
                        .setInterpolator(new LinearInterpolator())
                        .start();
            }
        };
        imgThumb.animate().rotationBy(360)
                .withEndAction(runnable)
                .setDuration(10000)
                .setInterpolator(new LinearInterpolator())
                .start();
    }

    private void stopAnim() {
        imgThumb.animate().cancel();
    }

    @Override
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        if (MusicServiceHelper.getCurrentSong() == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.img_likes:

                break;
            case R.id.img_play_pause:
                Long lastClick = (Long) v.getTag();
                long current = SystemClock.elapsedRealtime();
                if (lastClick != null && current - lastClick < MusicConfig.PLAY_PAUSE_CLICK_DELAY) {
                    return;
                }
                v.setTag(current);
                if (MusicServiceHelper.isPlaying()) {
                    MusicServiceHelper.sendAction(PAUSE_SONG);
                } else {
                    MusicServiceHelper.sendAction(PLAY_SONG);
                }
                break;
            case R.id.img_next:
                MusicServiceHelper.sendAction(NEXT_SONG);
                break;
            case R.id.control_layout:
                PlayMusicActivity.start(this, MusicServiceHelper.getCurrentSong());
                break;
        }
    }

    public void setShowControlLayout(boolean isShow) {
        setShowControlLayout(isShow, 250);
    }

    public void setShowControlLayout(boolean isShow, int duration) {
        if (MusicServiceHelper.getCurrentSong() != null && isShow) {
            AnimateUtils.animateHeight(controlLayout, duration, controlLayout.getHeight(), ScreenUtils.dp2px(56));
        } else {
            AnimateUtils.animateHeight(controlLayout, duration, controlLayout.getHeight(), 0);
        }
    }

    @Override
    protected int getContainerId() {
        return R.id.main_frame;
    }

    @Override
    protected void showToastOnBackPressed() {
        Toast.makeText(this, "Back again to exits!", Toast.LENGTH_SHORT).show();
    }
}