package com.aptech.mymusic.presentation.view.activity;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.NEXT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PAUSE_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.UPDATE_VIEW;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_CURRENT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_IS_PLAYING;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aptech.mymusic.R;
import com.aptech.mymusic.di.DataInjection;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.fragment.MainFragment;
import com.aptech.mymusic.presentation.view.service.MusicDelegate;
import com.aptech.mymusic.presentation.view.service.MusicService;
import com.aptech.mymusic.presentation.view.service.MusicStarter;
import com.bumptech.glide.Glide;
import com.mct.components.baseui.BaseActivity;

public class MainActivity extends BaseActivity {
    public static final int TWO_ITEM_CARD = 2;
    public static final int ONE_ITEM_CARD = 1;

    private SeekBar mSeekBar;
    private ImageView imgThumb;
    private TextView tvSongName;
    private TextView tvSingerName;
    private ImageView imgLikes;
    private ImageView imgPlayPause;
    private Runnable seekBarUpdateRunnable;

    BroadcastReceiver receiverFromMusicService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.e("ddd", "onReceive: " + bundle);
            if (bundle != null) {
                SongModel song = (SongModel) bundle.getSerializable(KEY_CURRENT_SONG);
                boolean isPlaying = bundle.getBoolean(KEY_IS_PLAYING);
                if (isPlaying) {
                    imgPlayPause.setImageResource(R.drawable.ic_pause);
                    startAnim();
                } else {
                    imgPlayPause.setImageResource(R.drawable.ic_play);
                    stopAnim();
                }
                if (song != null) {
                    tvSongName.setText(song.getName());
                    tvSingerName.setText(song.getSingerName());
                    if (tvSingerName.getVisibility() != View.VISIBLE) {
                        tvSingerName.setVisibility(View.VISIBLE);
                    }
                    Glide.with(MainActivity.this).load(song.getImageUrl()).into(imgThumb);
                    initSeekbar();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverFromMusicService, new IntentFilter(MusicDelegate.ACTION_UPDATE_VIEW));
        replaceFragment(new MainFragment());
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverFromMusicService);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        mSeekBar = findViewById(R.id.sb_music);
        mSeekBar.setOnTouchListener((v, event) -> true);

        imgThumb = findViewById(R.id.img_thumb);
        tvSongName = findViewById(R.id.tv_song_name);
        tvSingerName = findViewById(R.id.tv_singer_name);
        imgLikes = findViewById(R.id.img_likes);
        imgPlayPause = findViewById(R.id.img_play_pause);
        imgPlayPause.setOnClickListener(view -> {
            if (MusicService.isPlaying()) {
                MusicStarter.startService(PAUSE_SONG);
            } else {
                MusicStarter.startService(PLAY_SONG);
            }
        });
        findViewById(R.id.control_layout).setOnClickListener(v -> {
            if (DataInjection.provideMusicPreference().getLastSong() != null) {
                startActivity(new Intent(this, PlayMusicActivity.class));
            }
        });
        findViewById(R.id.img_next).setOnClickListener(v -> {
            if (!DataInjection.provideMusicPreference().getLastListSong().isEmpty()) {
                MusicStarter.startService(NEXT_SONG);
            }
        });
        MusicStarter.startService(UPDATE_VIEW);
    }

    private void initSeekbar() {
        mSeekBar.setMax(MusicService.getDuration());
        if (seekBarUpdateRunnable == null) {
            seekBarUpdateRunnable = () -> {
                if (MusicService.isPlaying()) {
                    mSeekBar.setProgress(MusicService.getCurrentPosition());
                    mSeekBar.postDelayed(seekBarUpdateRunnable, 1000);
                }
            };
        }
        mSeekBar.post(seekBarUpdateRunnable);
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
    protected int getContainerId() {
        return R.id.main_frame;
    }

    @Override
    protected void showToastOnBackPressed() {
        Toast.makeText(this, "Back again to exits!", Toast.LENGTH_SHORT).show();
    }
}