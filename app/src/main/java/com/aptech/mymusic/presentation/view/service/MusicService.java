package com.aptech.mymusic.presentation.view.service;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_PLAY_LIST;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_VIEW;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.NEXT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PAUSE_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PREV_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.STOP_SERVICE;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_CURRENT_LIST_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_CURRENT_MODE;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_CURRENT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_IS_PLAYING;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_LIST_SONG_OBJECT;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_MUSIC_ACTION;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_POSITION_DRAG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_POSITION_NEW_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_POSITION_TARGET;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_SONG_OBJECT;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_TIME_SEEK_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Mode.NORMAL;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Mode.REPEAT;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Mode.REPEAT_ONE;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Mode.SHUFFLE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
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

    private static LocalService mLocalService;

    public static boolean isPlaying() {
        if (mLocalService != null) {
            return mLocalService.mIsPlaying;
        }
        return false;
    }

    public static int getDuration() {
        if (mLocalService != null && mLocalService.mMediaPlayer != null) {
            return mLocalService.mMediaPlayer.getDuration();
        }
        return 0;
    }

    public static int getCurrentPosition() {
        if (mLocalService != null && mLocalService.mMediaPlayer != null) {
            return mLocalService.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Nullable
    public static SongModel getCurrentSong() {
        if (mLocalService != null) {
            return mLocalService.mSong;
        }
        return null;
    }

    @Nullable
    public static List<SongModel> getCurrentListSong() {
        if (mLocalService != null) {
            return mLocalService.mListSongTemp;
        }
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mLocalService == null) {
            mLocalService = new LocalService(this);
        }
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        return mLocalService.onStartCommand(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocalService != null) {
            mLocalService.release();
            mLocalService = null;
        }
    }

    private void sendNotificationMusic(SongModel song) {
        if (song == null) {
            return;
        }

        /*/
        createNotificationChannel();

        loadImage(song.getImageUrl(), image -> {
            Intent intent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, getFlag());

            RemoteViews remoteCollapsed = getRemoteViewCollapsed(song, mLocalService.mIsPlaying);

            RemoteViews remoteExpanded = getRemoteViewExpanded(song, mLocalService.mIsPlaying);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setOngoing(true)
                    .setAutoCancel(false)
                    .setSmallIcon(R.drawable.ic_app_small)
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
        /*/

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
                            .setMediaSession(mediaSession.getSessionToken())
                            .setShowActionsInCompactView(0, 1, 2))
                    .addAction(createAction(R.drawable.ic_prev, PREV_SONG))
                    .addAction(isPlaying() ? createAction(R.drawable.ic_pause, PAUSE_SONG)
                            : createAction(R.drawable.ic_play, PLAY_SONG))
                    .addAction(createAction(R.drawable.ic_next, NEXT_SONG))
                    .addAction(createAction(R.drawable.ic_cancel, STOP_SERVICE));

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
            channel.setDescription(description);
            channel.setSound(getSound(), new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build());
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /*/
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

    public static class LocalService {

        private static final String TAG = "LocalService";
        private WeakReference<MusicService> mService;
        private MusicPreference mMusicPreference;

        private Mode mMode;
        private SongModel mSong;
        private List<SongModel> mListSongOrigin;
        private List<SongModel> mListSongTemp;
        private boolean mIsPlaying;

        private MediaPlayer mMediaPlayer;
        private Toast mToast;

        LocalService(MusicService mService) {
            this.mService = new WeakReference<>(mService);
            this.mMusicPreference = DataInjection.provideMusicPreference();
            this.initData();
        }

        public int onStartCommand(Intent intent) {
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
            return START_NOT_STICKY;
        }

        private void handleAction(@NonNull Action action, Bundle bundle) {
            switch (action) {
                case UPDATE_VIEW:
                    updateView();
                    break;
                case PLAY_NEW_SONG:
                    playSongFromBundle(bundle);
                    break;
                case PLAY_NEW_LIST_SONG:
                    playListSongFromBundle(bundle);
                    break;
                case STOP_SERVICE:
                    stopService();
                    break;
                case PREV_SONG:
                    prevSong();
                    break;
                case PLAY_SONG:
                    playSong();
                    break;
                case PAUSE_SONG:
                    pauseSong();
                    break;
                case NEXT_SONG:
                    nextSong(false);
                    break;
                case SEEK_SONG:
                    seekSong(bundle.getInt(KEY_TIME_SEEK_SONG));
                    break;
                case SHUFFLE_SONG:
                    shuffleMode();
                    break;
                case REPEAT_SONG:
                    repeatMode();
                    break;
                case ADD_SONG:
                    addSong((SongModel) bundle.getSerializable(KEY_SONG_OBJECT), -1);
                    break;
                case REMOVE_SONG:
                    removeSong((SongModel) bundle.getSerializable(KEY_SONG_OBJECT));
                    break;
                case SWAP_SONG:
                    swapSong(bundle.getInt(KEY_POSITION_DRAG), bundle.getInt(KEY_POSITION_TARGET));
                    break;
            }
        }

        private void updateNotification() {
            if (mService != null && mService.get() != null) {
                mService.get().sendNotificationMusic(mSong);
            }
        }

        private void updatePlayList() {
            if (mService != null && mService.get() != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_CURRENT_LIST_SONG, new ArrayList<>(mListSongTemp));
                Intent intent = new Intent(ACTION_UPDATE_PLAY_LIST).putExtras(bundle);

                LocalBroadcastManager.getInstance(mService.get()).sendBroadcast(intent);
            }
        }

        private void updateView() {
            if (mService != null && mService.get() != null) {
                // send to activity => update view
                Bundle bundle = new Bundle();
                bundle.putBoolean(KEY_IS_PLAYING, mIsPlaying);
                bundle.putSerializable(KEY_CURRENT_SONG, mSong);
                bundle.putSerializable(KEY_CURRENT_MODE, mMode);
                Intent intent = new Intent(ACTION_UPDATE_VIEW).putExtras(bundle);

                LocalBroadcastManager.getInstance(mService.get()).sendBroadcast(intent);
            }
        }

        private void playSongFromBundle(@NonNull Bundle bundle) {
            SongModel song = (SongModel) bundle.getSerializable(KEY_SONG_OBJECT);
            if (song != null) {
                playSong(addSong(song, 0));
            }
        }

        @SuppressWarnings("unchecked")
        private void playListSongFromBundle(@NonNull Bundle bundle) {
            mListSongOrigin = (List<SongModel>) bundle.getSerializable(KEY_LIST_SONG_OBJECT);
            mListSongTemp = new ArrayList<>(mListSongOrigin);
            mMusicPreference.setLastListSong(mListSongOrigin);
            if (!mListSongOrigin.isEmpty()) {
                int index;
                // if the mode is shuffle => shuffle and play from index 0
                if (mMode == SHUFFLE) {
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
            playSong(index != 0 ? index - 1 : mListSongTemp.size() - 1);
        }

        private void playSong(int index) {
            if (!mIsPlaying || !mListSongTemp.get(index).equals(mSong)) {
                mSong = mListSongTemp.get(index);
                if (mMediaPlayer != null) {
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
            }
            playSong();
        }

        private void playSong() {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
                if (!mIsPlaying) {
                    mIsPlaying = true;
                    updateNotification();
                    updateView();
                }
            } else {
                initSong();
            }
        }

        private void pauseSong() {
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
                if (mIsPlaying) {
                    mIsPlaying = false;
                    updateNotification();
                    updateView();
                }
            }
        }

        private void nextSong(boolean isAutoNext) {
            int index = mListSongTemp.indexOf(mSong);
            switch (mMode) {
                case NORMAL:
                case SHUFFLE:
                    if (index == mListSongTemp.size() - 1 && isAutoNext) {
                        pauseSong();
                        break;
                    }
                    playSong(index != mListSongTemp.size() - 1 ? index + 1 : 0);
                    break;
                case REPEAT_ONE:
                    if (isAutoNext) {
                        seekSong(1);
                        break;
                    }
                case REPEAT:
                    playSong(index != mListSongTemp.size() - 1 ? index + 1 : 0);
                    break;
            }
        }

        private void seekSong(int time) {
            if (mMediaPlayer != null) {
                mIsPlaying = true;
                mMediaPlayer.seekTo(time);
                mMediaPlayer.start();
                updateView();
            }
        }

        private void shuffleMode() {
            // reset to origin list
            mListSongTemp = new ArrayList<>(mListSongOrigin);
            if (mMode == SHUFFLE) {
                mMode = NORMAL;
                showToast("Normal play mode");
            } else {
                mMode = SHUFFLE;
                shuffleSong(mListSongTemp);
                swapSong(0, mListSongTemp.indexOf(mSong));
                showToast("Random play mode");
            }
            mMusicPreference.setLastMode(mMode);
            updatePlayList();
            updateView();
        }

        private void repeatMode() {
            switch (mMode) {
                case NORMAL:
                case SHUFFLE:
                    mMode = REPEAT;
                    showToast("Current playlist is repeating");
                    break;
                case REPEAT:
                    mMode = REPEAT_ONE;
                    showToast("One-song looping mode");
                    break;
                case REPEAT_ONE:
                    mMode = NORMAL;
                    showToast("Normal play mode");
                    break;
            }
            mMusicPreference.setLastMode(mMode);
            updateView();
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
            if (mListSongOrigin.remove(song)) {
                mListSongTemp.remove(song);
                mMusicPreference.setLastListSong(mListSongOrigin);
            }
        }

        private void swapSong(int drag, int target) {
            try {
                if (mMode == SHUFFLE) {
                    Collections.swap(mListSongTemp, drag, target);
                } else {
                    Collections.swap(mListSongOrigin, drag, target);
                    Collections.swap(mListSongTemp, drag, target);
                }
            } catch (Throwable ignore) {
            }
        }

        private void initData() {
            this.mMode = mMusicPreference.getLastMode();
            this.mSong = mMusicPreference.getLastSong();
            this.mListSongOrigin = mMusicPreference.getLastListSong();
            this.mListSongTemp = new ArrayList<>(this.mListSongOrigin);
        }

        private void initSong() {
            if (mSong == null) {
                return;
            }
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            mIsPlaying = false;
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(mSong.getAudioUrl());
                Log.e(TAG, "songInit: prepare " + mSong.getName());
                mMediaPlayer.setOnPreparedListener(mediaPlayer -> {
                    Log.e(TAG, "songInit: success " + mSong.getName());
                    playSong();
                    mediaPlayer.setOnCompletionListener(mp -> nextSong(true));
                });
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateView();
            mMusicPreference.setLastSong(mSong);
        }


        private void shuffleSong(List<SongModel> songs) {
            Collections.shuffle(songs);
        }

        private int validateSongIndex(int index) {
            return index >= 0 && index <= mListSongOrigin.size() ? index : mListSongOrigin.size();
        }

        private void showToast(String text) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(mService.get(), text, Toast.LENGTH_SHORT);
            mToast.show();
        }

        public void release() {
            mIsPlaying = false;
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            updateView();
            if (mService != null && mService.get() != null) {
                mService.clear();
                mService = null;
            }
            mMusicPreference = null;
        }

    }

}
