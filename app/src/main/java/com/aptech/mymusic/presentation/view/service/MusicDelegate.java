package com.aptech.mymusic.presentation.view.service;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.aptech.mymusic.application.App;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.utils.JsonHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicDelegate {

    ///////////////////////////////////////////////////////////////////////////
    // ACTION
    ///////////////////////////////////////////////////////////////////////////
    public static final String ACTION_UPDATE_VIEW = "ACTION_UPDATE_VIEW";

    ///////////////////////////////////////////////////////////////////////////
    // KEY
    ///////////////////////////////////////////////////////////////////////////

    public static final String KEY_MUSIC_ACTION = "music_action";

    public static final String KEY_SONG_OBJECT = "song_object";
    public static final String KEY_LIST_SONG_OBJECT = "list_song_object";
    public static final String KEY_POSITION_NEW_SONG = "position_new_song";
    public static final String KEY_TIME_SEEK_SONG = "time_seek_song";

    ///////////////////////////////////////////////////////////////////////////
    // ENUM
    ///////////////////////////////////////////////////////////////////////////

    public enum Action {
        UPDATE_VIEW,
        PLAY_NEW_SONG,
        PLAY_NEW_LIST_SONG,
        STOP_SERVICE,

        PREV_SONG,
        PLAY_SONG,
        PAUSE_SONG,
        NEXT_SONG,
        SHUFFLE_SONG,
        REPEAT_SONG,
        SEEK_SONG,

    }

    public enum Mode {
        NORMAL,     // run to end of list song and stop
        SHUFFLE,    // shuffle list song and run first song (run like normal)
        REPEAT,     // run to end of list song and restart
        REPEAT_ONE, // run to end of song and restart

    }

    public enum MediaState {
        IDLE,
        PREPARED,
        PLAYING,
        PAUSED,
        RELEASE
    }

    public static class MediaBundle {

        private static MediaBundle instance;
        Mode mMode;
        SongModel mSong;
        List<SongModel> mListSongOrigin;
        List<SongModel> mListSongTemp;

        private MediaBundle() {
            this.mMode = getPreference().getLastMode();
            this.mSong = getPreference().getLastSong();
            this.mListSongOrigin = getPreference().getLastListSong();
            this.mListSongTemp = new ArrayList<>(this.mListSongOrigin);
        }

        static MediaBundle getInstance() {
            if (instance == null) {
                instance = new MediaBundle();
            }
            return instance;
        }

        int addSong(SongModel song, int i) {
            if (!mListSongTemp.contains(song)) {
                int index = validateSongIndex(i, mListSongOrigin.size());
                mListSongOrigin.add(index, song);
                mListSongTemp.add(index, song);
                getPreference().setLastListSong(mListSongOrigin);
                return index;
            }
            return mListSongTemp.indexOf(song);
        }

        void removeSong(SongModel song) {
            mListSongOrigin.remove(song);
            mListSongTemp.remove(song);
            getPreference().setLastListSong(mListSongOrigin);
        }

        void removeSongs(List<SongModel> songs) {
            mListSongOrigin.removeAll(songs);
            mListSongTemp.removeAll(songs);
            getPreference().setLastListSong(mListSongOrigin);
        }


        void swapSong(int drag, int target) {
            try {
                if (mMode == Mode.SHUFFLE) {
                    Collections.swap(mListSongTemp, drag, target);
                } else {
                    Collections.swap(mListSongOrigin, drag, target);
                    Collections.swap(mListSongTemp, drag, target);
                    getPreference().setLastListSong(mListSongOrigin);
                }
            } catch (Throwable ignore) {
            }
        }

        void shuffleTmpSong() {
            Collections.shuffle(mListSongTemp);
        }

        MusicPreference getPreference() {
            return MusicPreference.getInstance();
        }

        private int validateSongIndex(int index, int size) {
            return index >= 0 && index <= size ? index : size;
        }

    }

    public static class MediaTimer {

        private static final int MSG = 1;
        private static MediaTimer instance;
        private boolean mCancelled = true;
        private Action mAction;
        private final Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                mCancelled = true;
                MusicServiceHelper.sendAction(mAction);
            }
        };
        private long mStopTimeInFuture;

        private MediaTimer() {
        }

        static MediaTimer getInstance() {
            if (instance == null) {
                instance = new MediaTimer();
            }
            return instance;
        }

        public void startTimer(Action action, long millisInFuture) {
            mCancelled = false;
            mAction = action;
            mStopTimeInFuture = SystemClock.elapsedRealtime() + millisInFuture;
            mHandler.removeMessages(MSG);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), millisInFuture);
        }

        public void cancelTimer() {
            mCancelled = true;
            mHandler.removeMessages(MSG);
        }

        public boolean isCancelled() {
            return mCancelled;
        }

        public long getTimeRemaining() {
            if (isCancelled()) {
                return 0;
            }
            return mStopTimeInFuture - SystemClock.elapsedRealtime();
        }

        @NonNull
        public Pair<Integer, Integer> getTimeRemainingInPair() {
            if (isCancelled()) {
                return new Pair<>(0, 0);
            }
            int totalMinutes = (int) (getTimeRemaining() / (1000 * 60));
            int hours = totalMinutes / 60;
            int minutes = totalMinutes % 60;
            return new Pair<>(hours, minutes);
        }

    }

    public static class MusicPreference {

        static final String KEY_LAST_MODE = "KEY_LAST_MODE";
        static final String KEY_LAST_SONG = "KEY_LAST_SONG";
        static final String KEY_LAST_LIST_SONG = "KEY_LAST_LIST_SONG";
        static final String KEY_FAVORITE_SONG = "KEY_FAVORITE_SONG";
        private static final String PREF_NAME = "music_service_pref";
        private static MusicPreference instance;
        private final SharedPreferences mPreferences;

        private MusicPreference() {
            mPreferences = App.getInstance().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        }

        static MusicPreference getInstance() {
            if (instance == null) {
                instance = new MusicPreference();
            }
            return instance;
        }

        private SharedPreferences.Editor editor() {
            return mPreferences.edit();
        }

        Mode getLastMode() {
            return Mode.valueOf(mPreferences.getString(KEY_LAST_MODE, Mode.NORMAL.name()));
        }

        void setLastMode(@NonNull Mode mode) {
            editor().putString(KEY_LAST_MODE, mode.name()).apply();
        }

        SongModel getLastSong() {
            return JsonHelper.jsonToObj(mPreferences.getString(KEY_LAST_SONG, ""), SongModel.class);
        }

        void setLastSong(SongModel song) {
            editor().putString(KEY_LAST_SONG, JsonHelper.objToJson(song)).apply();
        }

        List<SongModel> getLastListSong() {
            return JsonHelper.jsonToList(mPreferences.getString(KEY_LAST_LIST_SONG, "[]"), SongModel.class);
        }

        void setLastListSong(List<SongModel> songs) {
            editor().putString(KEY_LAST_LIST_SONG, JsonHelper.objToJson(songs)).apply();
        }

        List<SongModel> getFavoriteSong() {
            return JsonHelper.jsonToList(mPreferences.getString(KEY_FAVORITE_SONG, "[]"), SongModel.class);
        }

        void setFavoriteSong(List<SongModel> songs) {
            editor().putString(KEY_FAVORITE_SONG, JsonHelper.objToJson(songs)).apply();
        }

        boolean isFavorite(SongModel song) {
            return getFavoriteSong().contains(song);
        }
    }
}
