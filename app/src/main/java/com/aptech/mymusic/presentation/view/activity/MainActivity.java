package com.aptech.mymusic.presentation.view.activity;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.NEXT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PAUSE_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.UPDATE_VIEW;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.fragment.MainFragment;
import com.aptech.mymusic.presentation.view.service.MusicDelegate;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.aptech.mymusic.utils.GlideUtils;
import com.mct.components.baseui.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final int TWO_ITEM_CARD = 2;
    public static final int ONE_ITEM_CARD = 1;

    private ViewGroup controlLayout;
    private SeekBar seekBar;
    private ImageView imgThumb;
    private TextView tvSongName;
    private TextView tvSingerName;
    private ImageView imgLikes;
    private ImageView imgPlayPause;
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
        setContentView(R.layout.activity_main);
        replaceFragment(new MainFragment());
        initUi();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverFromMusicService, new IntentFilter(MusicDelegate.ACTION_UPDATE_VIEW));
        MusicServiceHelper.sendAction(UPDATE_VIEW);
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
            imgPlayPause.setImageResource(R.drawable.ic_pause);
            startAnim();
        } else {
            imgPlayPause.setImageResource(R.drawable.ic_play);
            stopAnim();
        }
        if (song != null) {
            controlLayout.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
            tvSongName.setText(song.getName());
            tvSingerName.setText(song.getSingerName());
            tvSingerName.setVisibility(View.VISIBLE);
            GlideUtils.load(song.getImageUrl(), imgThumb);
            initSeekbar();
        } else {
            controlLayout.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
        }
    }

    private void initSeekbar() {
        seekBar.setMax(MusicServiceHelper.getDuration());
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
                MusicServiceHelper.sendAction(PLAY_SONG);
                startActivity(new Intent(this, PlayMusicActivity.class));
                break;
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