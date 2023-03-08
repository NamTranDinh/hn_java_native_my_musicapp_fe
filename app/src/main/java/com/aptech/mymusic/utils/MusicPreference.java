package com.aptech.mymusic.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.service.MusicDelegate.Mode;

import java.util.List;

public class MusicPreference {

    private static final String PREF_NAME = "music_service_pref";
    public static final String KEY_LAST_MODE = "KEY_LAST_MODE";
    public static final String KEY_LAST_SONG = "KEY_LAST_SONG";
    public static final String KEY_LAST_LIST_SONG = "KEY_LAST_LIST_SONG";

    private static MusicPreference instance;

    public static MusicPreference getInstance(Context context) {
        if (instance == null) {
            instance = new MusicPreference(context);
        }
        return instance;
    }

    private MusicPreference(@NonNull Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }

    private final SharedPreferences mPreferences;

    private SharedPreferences.Editor editor() {
        return mPreferences.edit();
    }

    public Mode getLastMode() {
        return Mode.valueOf(mPreferences.getString(KEY_LAST_MODE, Mode.NORMAL.name()));
    }

    public void setLastMode(@NonNull Mode mode) {
        editor().putString(KEY_LAST_MODE, mode.name()).apply();
    }

    public SongModel getLastSong() {
        return JsonHelper.jsonToObj(mPreferences.getString(KEY_LAST_SONG, ""), SongModel.class);
    }

    public void setLastSong(SongModel song) {
        editor().putString(KEY_LAST_SONG, JsonHelper.objToJson(song)).apply();
    }

    public List<SongModel> getLastListSong() {
        return JsonHelper.jsonToList(mPreferences.getString(KEY_LAST_LIST_SONG, "[]"), SongModel.class);
    }

    public void setLastListSong(List<SongModel> songs) {
        editor().putString(KEY_LAST_LIST_SONG, JsonHelper.objToJson(songs)).apply();
    }

}
