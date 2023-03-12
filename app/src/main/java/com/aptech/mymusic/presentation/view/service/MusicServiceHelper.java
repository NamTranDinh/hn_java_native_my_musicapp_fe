package com.aptech.mymusic.presentation.view.service;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_NEW_LIST_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_NEW_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.SEEK_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_LIST_SONG_OBJECT;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_MUSIC_ACTION;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_POSITION_NEW_SONG;
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

    public static boolean isPlaying() {
        return MusicService.isPlaying();
    }

    public static int getDuration() {
        return MusicService.getDuration();
    }

    public static int getCurrentPosition() {
        return MusicService.getCurrentPosition();
    }

    public static MusicDelegate.Mode getCurrentMode() {
        return MediaBundle.getInstance().mMode;
    }

    public static SongModel getCurrentSong() {
        return MediaBundle.getInstance().mSong;
    }

    public static List<SongModel> getCurrentListSong() {
        return MediaBundle.getInstance().mListSongTemp;
    }

    public static void addSong(SongModel song) {
        MediaBundle.getInstance().addSong(song, -1);
    }

    public static void removeSong(SongModel song) {
        MediaBundle.getInstance().removeSong(song);
    }

    public static void removeSongs(List<SongModel> songs) {
        MediaBundle.getInstance().removeSongs(songs);
    }

    public static void swapSong(int drag, int target) {
        MediaBundle.getInstance().swapSong(drag, target);
    }

    private static void startService(Bundle bundle) {
        Context context = App.getInstance();
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtras(bundle);
        context.startService(intent);
    }
}
