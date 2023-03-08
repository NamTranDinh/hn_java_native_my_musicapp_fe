package com.aptech.mymusic.presentation.view.activity;


import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_VIEW;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.ADD_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.NEXT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PAUSE_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_NEW_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PREV_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.REPEAT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.SHUFFLE_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.UPDATE_VIEW;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_CURRENT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_IS_PLAYING;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.aptech.mymusic.R;
import com.aptech.mymusic.di.DataInjection;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.adapter.PlayMusicViewPagerAdapter;
import com.aptech.mymusic.presentation.view.adapter.SongAdapter;
import com.aptech.mymusic.presentation.view.adapter.SongSpecialAdapter;
import com.aptech.mymusic.presentation.view.common.TextThumbSeekBar;
import com.aptech.mymusic.presentation.view.service.MusicService;
import com.aptech.mymusic.presentation.view.service.MusicStarter;
import com.aptech.mymusic.utils.SimpleRequest;
import com.aptech.mymusic.utils.ZoomOutPageTransformer;
import com.bumptech.glide.Glide;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.utils.ScreenUtils;

import me.relex.circleindicator.CircleIndicator3;

public class PlayMusicActivity extends BaseActivity implements SongAdapter.IOnClickSongItem, SongAdapter.IOnClickAddSong, SongSpecialAdapter.IOnClickSongItem {

    private ImageView mImgBackground;
    private Toolbar mToolbar;
    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCircleIndicator3;
    private TextThumbSeekBar mSeekBar;
    private ImageView imgShuffle;
    private ImageView imgRepeat;
    private ImageView imgPlayPause;
    private Runnable seekBarUpdateRunnable;

    BroadcastReceiver receiverFromMusicService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                SongModel song = (SongModel) bundle.getSerializable(KEY_CURRENT_SONG);
                boolean isPlaying = bundle.getBoolean(KEY_IS_PLAYING);
                imgPlayPause.setImageResource(isPlaying ? R.drawable.ic_pause_big : R.drawable.ic_play_big);
                mToolbar.setTitle(song.getName());
                mToolbar.setSubtitle(song.getSingerName());
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(song.getImageUrl())
                        .placeholder(R.drawable.custom_overlay_black)
                        .addListener(new SimpleRequest(mImgBackground))
                        .submit();
                initSeekbar();
                initMode();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_play_music);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverFromMusicService, new IntentFilter(ACTION_UPDATE_VIEW));
        initUi();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSeekBar.post(seekBarUpdateRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSeekBar.removeCallbacks(seekBarUpdateRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverFromMusicService);
    }

    private void initWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(false);
    }

    private void initUi() {
        mToolbar = findViewById(R.id.toolbar);
        mImgBackground = findViewById(R.id.img_background);
        mViewPager2 = findViewById(R.id.view_pager2);
        mCircleIndicator3 = findViewById(R.id.circle_indicator);
        mSeekBar = findViewById(R.id.seek_bar);

        ImageView imgNext = findViewById(R.id.img_next);
        ImageView imgPrev = findViewById(R.id.img_prev);
        imgNext.setOnClickListener(view -> MusicStarter.startService(NEXT_SONG));
        imgPrev.setOnClickListener(view -> MusicStarter.startService(PREV_SONG));
        imgShuffle = findViewById(R.id.img_shuffle);
        imgRepeat = findViewById(R.id.img_repeat);
        imgPlayPause = findViewById(R.id.img_play_pause);
        imgShuffle.setOnClickListener(view -> MusicStarter.startService(SHUFFLE_SONG));
        imgRepeat.setOnClickListener(view -> MusicStarter.startService(REPEAT_SONG));
        imgPlayPause.setOnClickListener(view -> {
            if (MusicService.isPlaying()) {
                MusicStarter.startService(PAUSE_SONG);
            } else {
                MusicStarter.startService(PLAY_SONG);
            }
        });

        // update margin
        ViewGroup viewGroup = findViewById(R.id.app_bar_layout);
        MarginLayoutParams lp = (MarginLayoutParams) viewGroup.getLayoutParams();
        lp.topMargin = ScreenUtils.getStatusBarHeight(this);
        viewGroup.setLayoutParams(lp);
    }

    private void initData() {
        mToolbar.setNavigationOnClickListener(v -> finish());
        PlayMusicViewPagerAdapter adapter = new PlayMusicViewPagerAdapter(PlayMusicActivity.this);
        mViewPager2.setAdapter(adapter);
        mViewPager2.setPageTransformer(new ZoomOutPageTransformer());
        mViewPager2.setCurrentItem(1);
        mCircleIndicator3.setViewPager(mViewPager2);
        if (MusicService.isPlaying()) {
            MusicStarter.startService(UPDATE_VIEW);
        }
    }

    private void initMode() {
        switch (DataInjection.provideMusicPreference().getLastMode()) {
            case NORMAL:
                imgShuffle.setImageResource(R.drawable.ic_shuffle_white);
                imgRepeat.setImageResource(R.drawable.ic_repeat_white);
                break;
            case SHUFFLE:
                imgShuffle.setImageResource(R.drawable.ic_shuffle_active);
                imgRepeat.setImageResource(R.drawable.ic_repeat_white);
                break;
            case REPEAT:
                imgShuffle.setImageResource(R.drawable.ic_shuffle_white);
                imgRepeat.setImageResource(R.drawable.ic_repeat_active);
                break;
            case REPEAT_ONE:
                imgShuffle.setImageResource(R.drawable.ic_shuffle_white);
                imgRepeat.setImageResource(R.drawable.ic_repeat_one_active);
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initSeekbar() {
        mSeekBar.setMax(MusicService.getDuration());
        mSeekBar.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                MusicStarter.seekSong(mSeekBar.getProgress());
            }
            return false;
        });
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

//    public void showPlaylistFragment() {
//        if (!isOpenPlaylist) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.anim_right_in, R.anim.anim_right_out, R.anim.anim_left_in, R.anim.anim_left_out);
//            transaction.add(R.id.rl_main_frame, new PlaylistSongFragment());
//            transaction.addToBackStack(PlaylistSongFragment.class.getName());
//            transaction.commit();
//            new Handler().postDelayed(() -> rlDefaultFrame.setVisibility(View.GONE), 150);
//            isOpenPlaylist = true;
//        }
//    }
//
//    public void hidePlaylistFragment() {
//        if (isOpenPlaylist) {
//            new Handler().postDelayed(() -> rlDefaultFrame.setVisibility(View.VISIBLE), 150);
//            getSupportFragmentManager().popBackStack();
//            isOpenPlaylist = false;
//        }
//    }

    @Override
    public void onClickSongItem(SongModel song) {
        MusicStarter.startService(PLAY_NEW_SONG, song);
    }

    @Override
    public void onClickAddSong(SongModel song) {
        Toast.makeText(this, "Added to playlist", Toast.LENGTH_SHORT).show();
        MusicStarter.startService(ADD_SONG, song);
    }

    @Override
    protected int getContainerId() {
        return 0;
    }

    @Override
    protected void showToastOnBackPressed() {
        finish();
    }

}