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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aptech.mymusic.R;
import com.aptech.mymusic.di.DataInjection;
import com.aptech.mymusic.domain.entity.CardModel;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.domain.entity.TopicModel;
import com.aptech.mymusic.presentation.view.common.TextThumbSeekBar;
import com.aptech.mymusic.presentation.view.fragment.musicplayer.MainPagerFragment;
import com.aptech.mymusic.presentation.view.service.MusicDelegate;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.aptech.mymusic.utils.BitmapUtils;
import com.aptech.mymusic.utils.GlideUtils;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.utils.ScreenUtils;

public class PlayMusicActivity extends BaseActivity {

    private static final String KEY_ARGS_MODEL = "args_model";
    private ImageView mImgBackground;
    private TextThumbSeekBar mSeekBar;
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

    public static void start(Context context, CardModel model) {
        if (model instanceof TopicModel) {
            Toast.makeText(context, "Un support Topic model for this action!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (model instanceof SongModel) {
            MusicServiceHelper.playSong((SongModel) model);
        }
        context.startActivity(
                new Intent(context, PlayMusicActivity.class).putExtra(KEY_ARGS_MODEL, model)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_play_music);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverFromMusicService, new IntentFilter(ACTION_UPDATE_VIEW));
        initUi();
        initData();
        replaceFragment(new MainPagerFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSeekBar.removeCallbacks(seekBarUpdateRunnable);
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
        mImgBackground = findViewById(R.id.img_background);
        mSeekBar = findViewById(R.id.seek_bar);

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
    }

    private void initData() {
        SongModel song = MusicServiceHelper.getCurrentSong();
        boolean isPlaying = MusicServiceHelper.isPlaying();
        if (song != null) {
            GlideUtils.load(song.getImageUrl(), image -> {
                if (image == null) {
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.custom_overlay_black);
                }
                mImgBackground.setImageBitmap(BitmapUtils.blur(getApplicationContext(), image, 25, 1));
            });
        }
        imgPlayPause.setImageResource(isPlaying ? R.drawable.ic_pause_circle : R.drawable.ic_play_circle);
        initSeekbar();
        initMode();
    }

    private void initMode() {
        MusicDelegate.Mode mode = MusicServiceHelper.getCurrentMode();
        if (mode == null) {
            mode = DataInjection.provideMusicPreference().getLastMode();
        }
        switch (mode) {
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
        mSeekBar.setMax(MusicServiceHelper.getDuration() == 0 ? 100 : MusicServiceHelper.getDuration());
        mSeekBar.setProgress(MusicServiceHelper.getCurrentPosition());
        mSeekBar.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                MusicServiceHelper.seekSong(mSeekBar.getProgress());
            }
            return false;
        });
        if (seekBarUpdateRunnable == null) {
            seekBarUpdateRunnable = () -> {
                if (MusicServiceHelper.isPlaying()) {
                    mSeekBar.setProgress(MusicServiceHelper.getCurrentPosition());
                    mSeekBar.postDelayed(seekBarUpdateRunnable, 500);
                }
            };
        }
        mSeekBar.removeCallbacks(seekBarUpdateRunnable);
        mSeekBar.post(seekBarUpdateRunnable);
    }

    @Override
    protected int getContainerId() {
        return R.id.main_music_frame;
    }

    @Override
    protected void showToastOnBackPressed() {
        finish();
    }

}