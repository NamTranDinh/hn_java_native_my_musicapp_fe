package com.aptech.mymusic.presentation.view.service;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.SEEK_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_LIST_SONG_OBJECT;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_MUSIC_ACTION;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_SONG_OBJECT;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_TIME_SEEK_SONG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aptech.mymusic.application.App;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.service.MusicDelegate;
import com.aptech.mymusic.presentation.view.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class MusicStarter {

    public static void startService(MusicDelegate.Action action) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, action);
        startService(bundle);
    }

    public static void startService(MusicDelegate.Action action, SongModel song) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, action);
        bundle.putSerializable(KEY_SONG_OBJECT, song);
        startService(bundle);
    }

    public static void startService(MusicDelegate.Action action, List<SongModel> songs) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, action);
        bundle.putSerializable(KEY_LIST_SONG_OBJECT, new ArrayList<>(songs));
        startService(bundle);
    }

    public static void seekSong(int time) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, SEEK_SONG);
        bundle.putInt(KEY_TIME_SEEK_SONG, time);
        startService(bundle);
    }

    private static void startService(Bundle bundle) {
        Context context = App.getInstance();
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtras(bundle);
        context.startService(intent);
    }
}
