package com.aptech.mymusic.presentation.view.service;

import static com.aptech.mymusic.presentation.view.service.MusicInteract.ACTION_UPDATE_PLAY_LIST;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.ACTION_UPDATE_VIEW;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.Action.NEXT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.Action.PAUSE_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.Action.PLAY_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.Action.PREV_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.Action.STOP_SERVICE;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_CURRENT_LIST_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_CURRENT_MODE;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_CURRENT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_IS_PLAYING;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_LAST_LIST_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_LAST_MODE;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_LAST_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_LIST_SONG_OBJECT;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_MUSIC_ACTION;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_POSITION_DRAG;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_POSITION_NEW_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_POSITION_TARGET;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_SONG_OBJECT;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_TIME_SEEK_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.Mode.NORMAL;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.Mode.REPEAT;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.Mode.REPEAT_ONE;
import static com.aptech.mymusic.presentation.view.service.MusicInteract.Mode.SHUFFLE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.activity.MainActivity;
import com.aptech.mymusic.presentation.view.broadcast.MusicReceiver;
import com.aptech.mymusic.presentation.view.service.MusicInteract.Action;
import com.aptech.mymusic.presentation.view.service.MusicInteract.Mode;
import com.aptech.mymusic.utils.JsonHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class MusicService extends Service {

    private static final String CHANNEL_ID = "MUSIC_APP";
    private static final int NOTIFY_ID = 123;

    private static LocalService mLocalService;

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
        createNotificationChannel();

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, getFlag());

        RemoteViews remoteCollapsed = getRemoteViewCollapsed(song, mLocalService.mIsPlaying);

        RemoteViews remoteExpanded = getRemoteViewExpanded(song, mLocalService.mIsPlaying);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_app_small)
                .setCustomContentView(remoteCollapsed)
                .setCustomBigContentView(remoteExpanded)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(getSound(), AudioManager.STREAM_NOTIFICATION)
                .build();
        loadImage(song.getImageUrl(), image -> {
            if (image != null) {
                remoteCollapsed.setImageViewBitmap(R.id.img_song_notification, image);
                remoteExpanded.setImageViewBitmap(R.id.img_song_notification, image);
            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, notification);
            startForeground(NOTIFY_ID, notification);
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
            channel.setSound(getSound(), new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build());
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @NonNull
    private RemoteViews getRemoteViewCollapsed(SongModel song, boolean isPlaying) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_music_play_notification_collapsed);

        remoteViews.setOnClickPendingIntent(R.id.img_next, getPendingIntent(this, NEXT_SONG));
        if (isPlaying) {
            remoteViews.setImageViewResource(R.id.img_play_pause, R.drawable.ic_pause);
            remoteViews.setOnClickPendingIntent(R.id.img_play_pause, getPendingIntent(this, PAUSE_SONG));
        } else {
            remoteViews.setImageViewResource(R.id.img_play_pause, R.drawable.ic_play);
            remoteViews.setOnClickPendingIntent(R.id.img_play_pause, getPendingIntent(this, PLAY_SONG));
        }
        remoteViews.setTextViewText(R.id.text_song_name, song.getName());
        remoteViews.setTextViewText(R.id.text_author_name, song.getSingerName());

        return remoteViews;
    }

    @NonNull
    private RemoteViews getRemoteViewExpanded(SongModel song, boolean isPlaying) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_music_play_notification_expanded);

        remoteViews.setOnClickPendingIntent(R.id.img_prev, getPendingIntent(this, PREV_SONG));
        remoteViews.setOnClickPendingIntent(R.id.img_next, getPendingIntent(this, NEXT_SONG));
        remoteViews.setOnClickPendingIntent(R.id.img_cancel, getPendingIntent(this, STOP_SERVICE));

        if (isPlaying) {
            remoteViews.setImageViewResource(R.id.img_play_pause, R.drawable.ic_pause);
            remoteViews.setOnClickPendingIntent(R.id.img_play_pause, getPendingIntent(this, PAUSE_SONG));
        } else {
            remoteViews.setImageViewResource(R.id.img_play_pause, R.drawable.ic_play);
            remoteViews.setOnClickPendingIntent(R.id.img_play_pause, getPendingIntent(this, PLAY_SONG));
        }
        remoteViews.setTextViewText(R.id.text_song_name, song.getName());
        remoteViews.setTextViewText(R.id.text_author_name, song.getSingerName());

        return remoteViews;
    }

    private PendingIntent getPendingIntent(Context context, Action action) {
        int offsetRC = 100;
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, action);
        Intent intent = new Intent(this, MusicReceiver.class)
                .setAction(KEY_MUSIC_ACTION)
                .putExtras(bundle);
        return PendingIntent.getBroadcast(context, offsetRC + action.ordinal(), intent, getFlag());
    }

    private int getFlag() {
        return PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
    }

    private Uri getSound() {
        return Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.empty_sound);
    }

    private void loadImage(String url, Consumer<Bitmap> onDone) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        onDone.accept(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        onDone.accept(getRoundedCornerBitmap(resource, 50));
                        return false;
                    }
                }).submit();
    }

    public Bitmap getRoundedCornerBitmap(@NonNull Bitmap bitmap, int cornerRadius) {
        // Create a new bitmap with the same dimensions as the original bitmap
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a new canvas and associate it with the new bitmap
        Canvas canvas = new Canvas(output);

        // Create a new paint object and set its antiAlias and filter flags to true
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        // Create a new rect object with the same dimensions as the bitmap
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        // Create a new rectF object with the same dimensions as the bitmap
        RectF rectF = new RectF(rect);

        // Create a new BitmapShader using the original bitmap and the CLAMP tile mode
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        // Set the shader of the paint object to the BitmapShader
        paint.setShader(bitmapShader);

        // Draw a round rect using the paint object and the rectF object
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

        // Recycle the original bitmap to free up memory
        bitmap.recycle();

        // Return the bitmap with rounded corners
        return output;
    }

    public static class LocalService {

        private static final String PREF_NAME = "music_service_pref";

        private WeakReference<MusicService> mService;

        private Mode mMode;
        private SongModel mSong;
        private List<SongModel> mListSongOrigin;
        private List<SongModel> mListSongTemp;
        private boolean mIsPlaying;

        private MediaPlayer mMediaPlayer;

        LocalService(MusicService mService) {
            this.mService = new WeakReference<>(mService);
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
            String songJson = bundle.getString(KEY_SONG_OBJECT);
            SongModel song = JsonHelper.jsonToObj(songJson, SongModel.class);
            if (song != null) {
                playSong(addSong(song, 0));
            }
        }

        private void playListSongFromBundle(@NonNull Bundle bundle) {
            String listSongJson = bundle.getString(KEY_LIST_SONG_OBJECT);
            mListSongOrigin = JsonHelper.jsonToList(listSongJson, SongModel.class);
            mListSongTemp = new ArrayList<>(mListSongOrigin);
            saveData(KEY_LAST_LIST_SONG, listSongJson);
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
                        stopService();
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
                Toast.makeText(mService.get(), "Chế độ phát bình thường", Toast.LENGTH_SHORT).show();
            } else {
                mMode = SHUFFLE;
                shuffleSong(mListSongTemp);
                swapSong(0, mListSongTemp.indexOf(mSong));
                Toast.makeText(mService.get(), "Chế độ phát ngẫu nhiên", Toast.LENGTH_SHORT).show();
            }
            saveData(KEY_LAST_MODE, mMode.name());
            updatePlayList();
        }

        private void repeatMode() {
            switch (mMode) {
                case NORMAL:
                case SHUFFLE:
                    mMode = REPEAT;
                    Toast.makeText(mService.get(), "Danh sách phát hiện tại đang lặp lại", Toast.LENGTH_SHORT).show();
                    break;
                case REPEAT:
                    mMode = REPEAT_ONE;
                    Toast.makeText(mService.get(), "Chế độ phát lặp lại 1 bài hát", Toast.LENGTH_SHORT).show();
                    break;
                case REPEAT_ONE:
                    mMode = NORMAL;
                    Toast.makeText(mService.get(), "Chế độ phát bình thường", Toast.LENGTH_SHORT).show();
                    break;
            }
            saveData(KEY_LAST_MODE, mMode.name());
        }

        private int addSong(SongModel song, int i) {
            if (!mListSongTemp.contains(song)) {
                int index = validateSongIndex(i);
                mListSongOrigin.add(index, song);
                mListSongTemp.add(index, song);
                saveData(KEY_LAST_LIST_SONG, JsonHelper.objToJson(mListSongOrigin));
                return index;
            }
            return mListSongTemp.indexOf(song);
        }

        private void swapSong(int drag, int target) {
            try {
                Collections.swap(mListSongTemp, drag, target);
                saveData(KEY_LAST_LIST_SONG, JsonHelper.objToJson(mListSongTemp));
            } catch (Throwable ignore) {
            }
        }

        private void initData() {
            SharedPreferences pref = getPref();
            this.mMode = Mode.valueOf(pref.getString(KEY_LAST_MODE, NORMAL.name()));
            this.mSong = JsonHelper.jsonToObj(pref.getString(KEY_LAST_SONG, ""), SongModel.class);
            this.mListSongOrigin = JsonHelper.jsonToList(pref.getString(KEY_LAST_LIST_SONG, "[]"), SongModel.class);
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
                Log.e("ddd", "songInit: prepare " + mSong.getName());
                mMediaPlayer.setOnPreparedListener(mediaPlayer -> {
                    Log.e("ddd", "songInit: success " + mSong.getName());
                    playSong();
                    mediaPlayer.setOnCompletionListener(mp -> {
                        Log.e("ddd", "songInit: complete " + mSong.getName());
                        nextSong(true);
                        updateView();
                    });
                });
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateView();
            saveData(KEY_LAST_SONG, JsonHelper.objToJson(mSong));
        }


        private void shuffleSong(List<SongModel> songs) {
            Collections.shuffle(songs);
        }

        private int validateSongIndex(int index) {
            return index >= 0 && index <= mListSongOrigin.size() ? index : mListSongOrigin.size();
        }

        private void saveData(String key, String value) {
            getPref().edit().putString(key, value).apply();
        }

        private SharedPreferences getPref() {
            return mService.get().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
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
        }

    }

}
