package com.aptech.mymusic.presentation.view.service;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_VIEW;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.NEXT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PAUSE_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PREV_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.STOP_SERVICE;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_LIST_SONG_OBJECT;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_MUSIC_ACTION;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_POSITION_NEW_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_SONG_OBJECT;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_TIME_SEEK_SONG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aptech.mymusic.R;
import com.aptech.mymusic.di.DataInjection;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.activity.PlayMusicActivity;
import com.aptech.mymusic.presentation.view.broadcast.MusicReceiver;
import com.aptech.mymusic.presentation.view.service.MusicDelegate.Action;
import com.aptech.mymusic.presentation.view.service.MusicDelegate.MediaState;
import com.aptech.mymusic.presentation.view.service.MusicDelegate.Mode;
import com.aptech.mymusic.utils.GlideUtils;
import com.aptech.mymusic.utils.MusicPreference;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicService extends Service {

    private static final String CHANNEL_ID = "MY_MUSIC_APP";
    private static final int NOTIFY_ID = 111;

    ///////////////////////////////////////////////////////////////////////////
    // Start Helper
    ///////////////////////////////////////////////////////////////////////////

    private static MusicPlayback sMusicPlayback;

    static boolean isPlaying() {
        if (sMusicPlayback != null) {
            return sMusicPlayback.mMediaState == MediaState.PLAYING;
        }
        return false;
    }

    static int getDuration() {
        if (sMusicPlayback != null) {
            MediaState state = sMusicPlayback.mMediaState;
            if (state == MediaState.PLAYING || state == MediaState.PAUSED) {
                return sMusicPlayback.mMediaPlayer.getDuration();
            }
        }
        return 0;
    }

    static int getCurrentPosition() {
        if (sMusicPlayback != null) {
            MediaState state = sMusicPlayback.mMediaState;
            if (state == MediaState.PLAYING || state == MediaState.PAUSED) {
                return sMusicPlayback.mMediaPlayer.getCurrentPosition();
            }
        }
        return 0;
    }

    @Nullable
    static Mode getCurrentMode() {
        if (sMusicPlayback != null) {
            return sMusicPlayback.mMode;
        }
        return null;
    }

    @Nullable
    static SongModel getCurrentSong() {
        if (sMusicPlayback != null) {
            return sMusicPlayback.mSong;
        }
        return null;
    }

    @Nullable
    static List<SongModel> getCurrentListSong() {
        if (sMusicPlayback != null) {
            return sMusicPlayback.mListSongTemp;
        }
        return null;
    }

    static void addSong(SongModel song) {
        if (sMusicPlayback != null) {
            sMusicPlayback.addSong(song, -1);
        }
    }

    static void removeSong(SongModel song) {
        if (sMusicPlayback != null) {
            sMusicPlayback.removeSong(song);
        }
    }

    static void swapSong(int drag, int target) {
        if (sMusicPlayback != null) {
            sMusicPlayback.swapSong(drag, target);
        }
    }

    static void setListSong(List<SongModel> songs) {
        if (sMusicPlayback != null) {
            sMusicPlayback.setListSong(songs);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Start Service
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate() {
        super.onCreate();
        sMusicPlayback = new MusicPlayback(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        return sMusicPlayback.onStartCommand(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sMusicPlayback.release();
        sMusicPlayback = null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        sMusicPlayback.savePreference();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        sMusicPlayback.savePreference();
    }

    private void sendNotificationMusic(SongModel song) {
        if (song == null) {
            return;
        }

        createNotificationChannel();

        Intent intent = new Intent(this, PlayMusicActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent mainIntent = PendingIntent.getActivity(this, 123, intent, getFlag());

        MediaSessionCompat mediaSession = new MediaSessionCompat(this, CHANNEL_ID);
        mediaSession.setActive(true);

        GlideUtils.load(song.getImageUrl(), image -> {
            if (image == null) {
                image = BitmapFactory.decodeResource(getResources(), R.drawable.background_header_layout);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSilent(true)
                    .setOngoing(true)
                    .setAutoCancel(false)
                    .setSmallIcon(R.drawable.ic_musical_note)
                    .setContentTitle(song.getName())
                    .setContentText(song.getSingerName())
                    .setLargeIcon(image)
                    .setContentIntent(mainIntent)
                    .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setMediaSession(mediaSession.getSessionToken()))
                    .addAction(createAction(R.drawable.ic_prev, PREV_SONG))
                    .addAction(isPlaying() ? createAction(R.drawable.ic_pause, PAUSE_SONG)
                            : createAction(R.drawable.ic_play, PLAY_SONG))
                    .addAction(createAction(R.drawable.ic_next, NEXT_SONG))
                    .addAction(createAction(R.drawable.ic_cancel, STOP_SERVICE));

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, builder.build());
            startForeground(NOTIFY_ID, builder.build());
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setShowBadge(false);
            channel.setDescription(description);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setSound(getSound(), new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build());
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /*/
    private void sendNotificationMusic(SongModel song) {
        if (song == null) {
            return;
        }
        createNotificationChannel();

        GlideUtils.load(song.getImageUrl(), image -> {
            if (image == null) {
                image = BitmapFactory.decodeResource(getResources(), R.drawable.background_header_layout);
            }
            Intent intent = new Intent(this, PlayMusicActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, getFlag());

            RemoteViews remoteCollapsed = getRemoteViewCollapsed(song, isPlaying());

            RemoteViews remoteExpanded = getRemoteViewExpanded(song, isPlaying());

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSilent(true)
                    .setOngoing(true)
                    .setAutoCancel(false)
                    .setSmallIcon(R.drawable.ic_musical_note)
                    .setCustomContentView(remoteCollapsed)
                    .setCustomBigContentView(remoteExpanded)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setSound(getSound(), AudioManager.STREAM_NOTIFICATION)
                    .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                    .build();
            remoteCollapsed.setImageViewBitmap(R.id.img_song_notification, image);
            remoteExpanded.setImageViewBitmap(R.id.img_song_notification, image);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, notification);
            startForeground(NOTIFY_ID, notification);
        });
    }

    @NonNull
    private RemoteViews getRemoteViewCollapsed(SongModel song, boolean isPlaying) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_music_play_notification_collapsed);

        remoteViews.setOnClickPendingIntent(R.id.img_next, getPendingIntent(NEXT_SONG));

        if (isPlaying) {
            remoteViews.setImageViewResource(R.id.img_play_pause, R.drawable.ic_pause);
            remoteViews.setOnClickPendingIntent(R.id.img_play_pause, getPendingIntent(PAUSE_SONG));
        } else {
            remoteViews.setImageViewResource(R.id.img_play_pause, R.drawable.ic_play);
            remoteViews.setOnClickPendingIntent(R.id.img_play_pause, getPendingIntent(PLAY_SONG));
        }
        remoteViews.setTextViewText(R.id.text_song_name, song.getName());
        remoteViews.setTextViewText(R.id.text_author_name, song.getSingerName());

        return remoteViews;
    }

    @NonNull
    private RemoteViews getRemoteViewExpanded(SongModel song, boolean isPlaying) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_music_play_notification_expanded);

        remoteViews.setOnClickPendingIntent(R.id.img_prev, getPendingIntent(PREV_SONG));
        remoteViews.setOnClickPendingIntent(R.id.img_next, getPendingIntent(NEXT_SONG));
        remoteViews.setOnClickPendingIntent(R.id.img_cancel, getPendingIntent(STOP_SERVICE));

        if (isPlaying) {
            remoteViews.setImageViewResource(R.id.img_play_pause, R.drawable.ic_pause);
            remoteViews.setOnClickPendingIntent(R.id.img_play_pause, getPendingIntent(PAUSE_SONG));
        } else {
            remoteViews.setImageViewResource(R.id.img_play_pause, R.drawable.ic_play);
            remoteViews.setOnClickPendingIntent(R.id.img_play_pause, getPendingIntent(PLAY_SONG));
        }
        remoteViews.setTextViewText(R.id.text_song_name, song.getName());
        remoteViews.setTextViewText(R.id.text_author_name, song.getSingerName());

        return remoteViews;
    }
    /*/

    @NonNull
    private NotificationCompat.Action createAction(int icon, @NonNull Action action) {
        return new NotificationCompat.Action(icon, action.name(), getPendingIntent(action));
    }

    private PendingIntent getPendingIntent(Action action) {
        int offsetRC = 100;
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, action);
        Intent intent = new Intent(this, MusicReceiver.class)
                .setAction(KEY_MUSIC_ACTION)
                .putExtras(bundle);
        return PendingIntent.getBroadcast(this, offsetRC + action.ordinal(), intent, getFlag());
    }

    private int getFlag() {
        return PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
    }

    private Uri getSound() {
        return Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.empty_sound);
    }

    private static class MusicPlayback {

        private static final String TAG = "MusicPlayback";
        private WeakReference<MusicService> mService;
        private MusicPreference mMusicPreference;

        private Mode mMode;
        private SongModel mSong;
        private List<SongModel> mListSongOrigin;
        private List<SongModel> mListSongTemp;

        private MediaState mMediaState;
        private MediaPlayer mMediaPlayer;
        private Toast mToast;
        private OnAudioFocusChangeListener mAudiofocusListener;
        private AudioFocusRequest mAudioFocusRequest;
        private AudioManager mAudioManager;
        private boolean mHasAudioFocus;

        private MusicPlayback(MusicService mService) {
            this.mService = new WeakReference<>(mService);
            this.mMusicPreference = DataInjection.provideMusicPreference();
            this.mMediaState = MediaState.IDLE;
            this.mMediaPlayer = new MediaPlayer();
            this.mMode = mMusicPreference.getLastMode();
            this.mSong = mMusicPreference.getLastSong();
            this.mListSongOrigin = mMusicPreference.getLastListSong();
            this.mListSongTemp = new ArrayList<>(this.mListSongOrigin);
        }

        private int onStartCommand(Intent intent) {
            Bundle bundle;
            Serializable s;
            if (intent == null) {
                return START_NOT_STICKY;
            }
            if ((bundle = intent.getExtras()) == null) {
                return START_NOT_STICKY;
            }
            if ((s = bundle.getSerializable(KEY_MUSIC_ACTION)) instanceof Action) {
                handleAction((Action) s, bundle);
            }
            return START_STICKY;
        }

        // @formatter:off
        private void handleAction(@NonNull Action action, Bundle bundle) {
            switch (action) {
                case UPDATE_VIEW:        updateView();                                break;
                case PLAY_NEW_SONG:      playSongFromBundle(bundle);                  break;
                case PLAY_NEW_LIST_SONG: playListSongFromBundle(bundle);              break;
                case STOP_SERVICE:       stopService();                               break;
                case PREV_SONG:          prevSong();                                  break;
                case PLAY_SONG:          playSong();                                  break;
                case PAUSE_SONG:         pauseSong();                                 break;
                case NEXT_SONG:          nextSong(false);                             break;
                case SHUFFLE_SONG:       shuffleMode();                               break;
                case REPEAT_SONG:        repeatMode();                                break;
                case SEEK_SONG:          seekSong(bundle.getInt(KEY_TIME_SEEK_SONG)); break;
            }
        }
        // @formatter:on

        private void updateNotification() {
            if (mService != null && mService.get() != null) {
                mService.get().sendNotificationMusic(mSong);
            }
        }

        private void updateView() {
            if (mService != null && mService.get() != null) {
                LocalBroadcastManager.getInstance(mService.get()).sendBroadcast(new Intent(ACTION_UPDATE_VIEW));
            }
        }

        private void playSongFromBundle(@NonNull Bundle bundle) {
            SongModel song = (SongModel) bundle.getSerializable(KEY_SONG_OBJECT);
            if (song != null) {
                // add song below current song in the list
                playSong(addSong(song, mListSongTemp.indexOf(mSong) + 1));
            }
        }

        @SuppressWarnings("unchecked")
        private void playListSongFromBundle(@NonNull Bundle bundle) {
            mListSongOrigin = (List<SongModel>) bundle.getSerializable(KEY_LIST_SONG_OBJECT);
            mListSongTemp = new ArrayList<>(mListSongOrigin);
            if (!mListSongOrigin.isEmpty()) {
                int index;
                // if the mode is shuffle => shuffle and play from index 0
                if (mMode == Mode.SHUFFLE) {
                    index = 0;
                    shuffleSong(mListSongTemp);
                } else {
                    index = validateSongIndex(bundle.getInt(KEY_POSITION_NEW_SONG, 0));
                }
                playSong(index);
            }
        }

        private void stopService() {
            if (mService != null && mService.get() != null) {
                mService.get().stopSelf();
            }
        }

        private void prevSong() {
            int index = mListSongTemp.indexOf(mSong);
            playSong(index > 0 ? index - 1 : mListSongTemp.size() - 1);
        }

        private void playSong(int index) {
            if (!mListSongTemp.get(index).equals(mSong)) {
                mSong = mListSongTemp.get(index);
                applyMediaState(MediaState.IDLE);
                playSong();
            }
        }

        private void playSong() {
            if (mMediaState == MediaState.IDLE) {
                try {
                    mMediaPlayer.reset();
                    mMediaPlayer.setAudioAttributes(getAudioAttributes());
                    mMediaPlayer.setDataSource(mSong.getAudioUrl());
                    mMediaPlayer.setOnCompletionListener(null);
                    mMediaPlayer.setOnPreparedListener(mediaPlayer -> {
                        Log.i(TAG, "PrepareAsync done : " + mSong.getName());
                        mediaPlayer.setOnCompletionListener(mp -> nextSong(true));
                        applyMediaState(MediaState.PREPARED);
                        playSong();
                    });
                    Log.i(TAG, "Start prepareAsync: " + mSong.getName());
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    Log.e(TAG, "PrepareAsync error: ", e);
                }
                mMusicPreference.setLastSong(mSong);
                updateView();
            }
            if (mMediaState == MediaState.PREPARED || mMediaState == MediaState.PAUSED) {
                // requestAudioFocus();
                if (requestAudioFocus()) {
                    applyMediaState(MediaState.PLAYING);
                    mMediaPlayer.start();
                    updateNotification();
                    updateView();
                }
            }
        }

        private void pauseSong() {
            if (mMediaState == MediaState.PLAYING) {
                applyMediaState(MediaState.PAUSED);
                mMediaPlayer.pause();
                updateNotification();
                updateView();
            }
        }

        private void nextSong(boolean isAutoNext) {
            if (mListSongTemp.isEmpty()) {
                return;
            }
            int index = mListSongTemp.indexOf(mSong);
            if (index == -1 || mSong == null) {
                playSong(0);
                return;
            }
            switch (mMode) {
                case NORMAL:
                case SHUFFLE:
                    if (index == mListSongTemp.size() - 1 && isAutoNext) {
                        pauseSong();
                    }
                    break;
                case REPEAT_ONE:
                    if (isAutoNext) {
                        seekSong(1);
                    }
                    break;
                case REPEAT:
                    break;
            }
            playSong(index < mListSongTemp.size() - 1 ? index + 1 : 0);
        }

        private void shuffleMode() {
            // reset to origin list
            mListSongTemp = new ArrayList<>(mListSongOrigin);
            if (mMode == Mode.SHUFFLE) {
                mMode = Mode.NORMAL;
                showToast("Normal play mode");
            } else {
                mMode = Mode.SHUFFLE;
                shuffleSong(mListSongTemp);
                swapSong(0, mListSongTemp.indexOf(mSong));
                showToast("Random play mode");
            }
            mMusicPreference.setLastMode(mMode);
            updateView();
        }

        private void repeatMode() {
            switch (mMode) {
                case NORMAL:
                case SHUFFLE:
                    mMode = Mode.REPEAT;
                    showToast("Current playlist is repeating");
                    break;
                case REPEAT:
                    mMode = Mode.REPEAT_ONE;
                    showToast("One-song looping mode");
                    break;
                case REPEAT_ONE:
                    mMode = Mode.NORMAL;
                    showToast("Normal play mode");
                    break;
            }
            mMusicPreference.setLastMode(mMode);
            updateView();
        }

        private void seekSong(int time) {
            if (mMediaState == MediaState.PLAYING || mMediaState == MediaState.PAUSED) {
                applyMediaState(MediaState.PLAYING);
                mMediaPlayer.seekTo(time);
                mMediaPlayer.start();
                updateView();
            }
        }

        private int addSong(SongModel song, int i) {
            if (!mListSongTemp.contains(song)) {
                int index = validateSongIndex(i);
                mListSongOrigin.add(index, song);
                mListSongTemp.add(index, song);
                mMusicPreference.setLastListSong(mListSongOrigin);
                return index;
            }
            return mListSongTemp.indexOf(song);
        }

        private void removeSong(SongModel song) {
            mListSongOrigin.remove(song);
            mListSongTemp.remove(song);
            mMusicPreference.setLastListSong(mListSongOrigin);
        }

        private void swapSong(int drag, int target) {
            try {
                if (mMode == Mode.SHUFFLE) {
                    Collections.swap(mListSongTemp, drag, target);
                } else {
                    Collections.swap(mListSongOrigin, drag, target);
                    Collections.swap(mListSongTemp, drag, target);
                    mMusicPreference.setLastListSong(mListSongOrigin);
                }
            } catch (Throwable ignore) {
            }
        }

        private void setListSong(List<SongModel> songs) {
            mListSongOrigin = new ArrayList<>(songs);
            mListSongTemp = songs;
            mMusicPreference.setLastListSong(mListSongOrigin);
        }

        private boolean requestAudioFocus() {
            if (!mHasAudioFocus) {
                mAudioManager = (AudioManager) mService.get().getSystemService(Context.AUDIO_SERVICE);
                mAudiofocusListener = focusChange -> {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            // Pause playback
                            Log.d(TAG, "AudioFocus Changed: AUDIOFOCUS_LOSS_TRANSIENT");
                            pauseSong();
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            // Resume playback
                            Log.d(TAG, "AudioFocus Changed: AUDIOFOCUS_GAIN");
                            playSong();
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS:
                            // Stop playback
                            Log.d(TAG, "AudioFocus Changed: AUDIOFOCUS_LOSS");
                            abandonAudioFocus();
                            pauseSong();
                            break;
                    }
                };
                int result;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mAudioFocusRequest = new AudioFocusRequest
                            .Builder(AudioManager.AUDIOFOCUS_GAIN)
                            .setAudioAttributes(getAudioAttributes())
                            .setOnAudioFocusChangeListener(mAudiofocusListener)
                            .build();
                    result = mAudioManager.requestAudioFocus(mAudioFocusRequest);
                } else {
                    result = mAudioManager.requestAudioFocus(mAudiofocusListener,
                            AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN);
                }
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mHasAudioFocus = true;
                }
            }
            Log.d(TAG, "AudioFocus Request: Has focus is " + mHasAudioFocus);
            return mHasAudioFocus;
        }

        public void abandonAudioFocus() {
            if (mHasAudioFocus) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest);
                } else {
                    mAudioManager.abandonAudioFocus(mAudiofocusListener);
                }
                mAudioFocusRequest = null;
                mAudiofocusListener = null;
                mHasAudioFocus = false;
            }
        }

        private AudioAttributes getAudioAttributes() {
            return new AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();
        }

        private void shuffleSong(List<SongModel> songs) {
            Collections.shuffle(songs);
        }

        private int validateSongIndex(int index) {
            return index >= 0 && index <= mListSongOrigin.size() ? index : mListSongOrigin.size();
        }

        private void applyMediaState(MediaState state) {
            if (mMediaState == state) {
                return;
            }
            Log.w(TAG, "Media State change: " + mMediaState + " --> " + state);
            switch (mMediaState) {
                case IDLE:
                    if (state == MediaState.PREPARED || state == MediaState.RELEASE) {
                        mMediaState = state;
                        return;
                    }
                    throw new RuntimeException("Idle state only acp state (Prepare or Release)");
                case PREPARED:
                    if (state != MediaState.PAUSED) {
                        mMediaState = state;
                        return;
                    }
                    throw new RuntimeException("Prepare state not acp state (Pause)");
                case PLAYING:
                case PAUSED:
                    if (state != MediaState.PREPARED) {
                        mMediaState = state;
                        return;
                    }
                    throw new RuntimeException("Start or Pause state not acp state (Prepare)");
                case RELEASE:
                    throw new RuntimeException("Release state can't set again!");
            }
        }

        private void savePreference() {
            mMusicPreference.setLastMode(mMode);
            mMusicPreference.setLastSong(mSong);
            mMusicPreference.setLastListSong(mListSongOrigin);
        }

        private void showToast(String text) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(mService.get(), text, Toast.LENGTH_SHORT);
            mToast.show();
        }

        public void release() {
            savePreference();
            applyMediaState(MediaState.RELEASE);
            mMediaPlayer.release();
            mMediaPlayer = null;
            mMusicPreference = null;
            updateView();
            if (mService != null && mService.get() != null) {
                mService.clear();
                mService = null;
            }
        }
    }

}
