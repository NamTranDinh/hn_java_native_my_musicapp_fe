package com.aptech.mymusic.presentation.view.service;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.ADD_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_NEW_LIST_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_NEW_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.REMOVE_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.SEEK_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.SWAP_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_LIST_SONG_OBJECT;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_MUSIC_ACTION;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_POSITION_DRAG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_POSITION_NEW_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_POSITION_TARGET;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_SONG_OBJECT;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_TIME_SEEK_SONG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aptech.mymusic.application.App;
import com.aptech.mymusic.domain.entity.SongModel;

import java.util.ArrayList;
import java.util.List;

public class MusicServiceHelper {

    public static void sendAction(MusicDelegate.Action action) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, action);
        startService(bundle);
    }

    public static void playSong(SongModel song) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, PLAY_NEW_SONG);
        bundle.putSerializable(KEY_SONG_OBJECT, song);
        startService(bundle);
    }

    public static void playListSong(List<SongModel> songs, int index) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, PLAY_NEW_LIST_SONG);
        bundle.putSerializable(KEY_LIST_SONG_OBJECT, new ArrayList<>(songs));
        bundle.putInt(KEY_POSITION_NEW_SONG, index);
        startService(bundle);
    }

    public static void seekSong(int time) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, SEEK_SONG);
        bundle.putInt(KEY_TIME_SEEK_SONG, time);
        startService(bundle);
    }

    public static void addSong(SongModel song) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, ADD_SONG);
        bundle.putSerializable(KEY_SONG_OBJECT, song);
        startService(bundle);
    }

    public static void removeSong(SongModel song) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, REMOVE_SONG);
        bundle.putSerializable(KEY_SONG_OBJECT, song);
        startService(bundle);
    }

    public static void swapSong(int drag, int target) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MUSIC_ACTION, SWAP_SONG);
        bundle.putInt(KEY_POSITION_DRAG, drag);
        bundle.putInt(KEY_POSITION_TARGET, target);
        startService(bundle);
    }

    private static void startService(Bundle bundle) {
        Context context = App.getInstance();
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtras(bundle);
        context.startService(intent);
    }
}
