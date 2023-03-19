package com.aptech.mymusic.presentation.view.activity;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_VIEW;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.NEXT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PAUSE_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PREV_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.REPEAT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.SHUFFLE_SONG;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.common.TextThumbSeekBar;
import com.aptech.mymusic.presentation.view.fragment.musicplayer.MainPagerFragment;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.aptech.mymusic.utils.BitmapUtils;
import com.aptech.mymusic.utils.GlideUtils;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.utils.ScreenUtils;

import java.util.List;

public class PlayMusicActivity extends BaseActivity {

    private static final String KEY_ARGS_MODEL = "args_model";
    private ImageView imgBackground;
    private TextThumbSeekBar seekBar;
    private ImageView imgShuffle;
    private ImageView imgRepeat;
    private ImageView imgPlayPause;
    private Runnable seekBarUpdateRunnable;

    BroadcastReceiver receiverFromMusicService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            initData();
        }
    };

    public static void start(@NonNull Context context, SongModel song) {
        MusicServiceHelper.playSong(song);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(context,
                R.anim.slide_in_bottom, R.anim.slide_out_top_10);
        Intent intent = new Intent(context, PlayMusicActivity.class).putExtra(KEY_ARGS_MODEL, song);
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    public static void start(@NonNull Context context, List<SongModel> songs, int index) {
        MusicServiceHelper.playListSong(songs, index);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(context,
                R.anim.slide_in_bottom, R.anim.slide_out_top_10);
        Intent intent = new Intent(context, PlayMusicActivity.class).putExtra(KEY_ARGS_MODEL, songs.get(index));
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverFromMusicService, new IntentFilter(ACTION_UPDATE_VIEW));
        initUi();
        initData();
        replaceFragment(new MainPagerFragment());
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

    public void setAppearanceLightStatusBars(boolean isLight) {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(isLight);
    }

    private void initUi() {
        imgBackground = findViewById(R.id.img_background);
        seekBar = findViewById(R.id.seek_bar);

        ImageView imgNext = findViewById(R.id.img_next);
        ImageView imgPrev = findViewById(R.id.img_prev);
        imgNext.setOnClickListener(view -> MusicServiceHelper.sendAction(NEXT_SONG));
        imgPrev.setOnClickListener(view -> MusicServiceHelper.sendAction(PREV_SONG));
        imgShuffle = findViewById(R.id.img_shuffle);
        imgRepeat = findViewById(R.id.img_repeat);
        imgPlayPause = findViewById(R.id.img_play_pause);
        imgShuffle.setOnClickListener(view -> MusicServiceHelper.sendAction(SHUFFLE_SONG));
        imgRepeat.setOnClickListener(view -> MusicServiceHelper.sendAction(REPEAT_SONG));
        imgPlayPause.setOnClickListener(view -> {
            if (MusicServiceHelper.isPlaying()) {
                MusicServiceHelper.sendAction(PAUSE_SONG);
            } else {
                MusicServiceHelper.sendAction(PLAY_SONG);
            }
        });

        // update margin
        ViewGroup viewGroup = findViewById(R.id.main_music_frame);
        MarginLayoutParams lp = (MarginLayoutParams) viewGroup.getLayoutParams();
        lp.topMargin = ScreenUtils.getStatusBarHeight(this);
        viewGroup.setLayoutParams(lp);

        setAppearanceLightStatusBars(false);
    }

    private void initData() {
        SongModel song = MusicServiceHelper.getCurrentSong();
        boolean isPlaying = MusicServiceHelper.isPlaying();
        if (song != null) {
            GlideUtils.load(song.getImageUrl(), image -> {
                if (image == null) {
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.custom_overlay_black);
                }
                imgBackground.setImageBitmap(BitmapUtils.blur(getApplicationContext(), image, 25, 1));
            });
        }
        imgPlayPause.setImageResource(isPlaying ? R.drawable.ic_pause_circle : R.drawable.ic_play_circle);
        initSeekbar();
        initMode();
    }

    private void initMode() {
        switch (MusicServiceHelper.getCurrentMode()) {
            case NORMAL:
                imgShuffle.setSelected(false);
                imgRepeat.setSelected(false);
                imgShuffle.setImageResource(R.drawable.ic_shuffle);
                imgRepeat.setImageResource(R.drawable.ic_repeat);
                break;
            case SHUFFLE:
                imgShuffle.setSelected(true);
                imgRepeat.setSelected(false);
                imgShuffle.setImageResource(R.drawable.ic_shuffle);
                imgRepeat.setImageResource(R.drawable.ic_repeat);
                break;
            case REPEAT:
                imgShuffle.setSelected(false);
                imgRepeat.setSelected(true);
                imgShuffle.setImageResource(R.drawable.ic_shuffle);
                imgRepeat.setImageResource(R.drawable.ic_repeat);
                break;
            case REPEAT_ONE:
                imgShuffle.setSelected(false);
                imgRepeat.setSelected(true);
                imgShuffle.setImageResource(R.drawable.ic_shuffle);
                imgRepeat.setImageResource(R.drawable.ic_repeat_one);
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initSeekbar() {
        seekBar.setMax(MusicServiceHelper.getDuration() == 0 ? 100 : MusicServiceHelper.getDuration());
        seekBar.setProgress(MusicServiceHelper.getCurrentPosition());
        seekBar.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                MusicServiceHelper.seekSong(seekBar.getProgress());
            }
            return false;
        });
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

    @Override
    protected int getContainerId() {
        return R.id.main_music_frame;
    }

    @Override
    protected void showToastOnBackPressed() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_top_10, R.anim.slide_out_bottom);
    }

}