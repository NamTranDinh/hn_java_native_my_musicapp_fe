package com.aptech.mymusic.presentation.view.service;

public class MusicDelegate {

    ///////////////////////////////////////////////////////////////////////////
    // ACTION
    ///////////////////////////////////////////////////////////////////////////
    public static final String ACTION_UPDATE_VIEW = "ACTION_UPDATE_VIEW";
    public static final String ACTION_UPDATE_PLAY_LIST = "ACTION_UPDATE_PLAY_LIST";

    ///////////////////////////////////////////////////////////////////////////
    // KEY
    ///////////////////////////////////////////////////////////////////////////

    public static final String KEY_MUSIC_ACTION = "KEY_MUSIC_ACTION";
    public static final String KEY_IS_PLAYING = "KEY_IS_PLAYING";
    public static final String KEY_CURRENT_MODE = "KEY_CURRENT_MODE";
    public static final String KEY_CURRENT_SONG = "KEY_CURRENT_SONG";
    public static final String KEY_CURRENT_LIST_SONG = "KEY_CURRENT_LIST_SONG";

    public static final String KEY_SONG_OBJECT = "KEY_SONG_OBJECT";
    public static final String KEY_LIST_SONG_OBJECT = "KEY_LIST_SONG_OBJECT";
    public static final String KEY_POSITION_NEW_SONG = "KEY_POSITION_NEW_SONG";
    public static final String KEY_TIME_SEEK_SONG = "KEY_TIME_SEEK_SONG";
    public static final String KEY_POSITION_DRAG = "KEY_POSITION_DRAG";
    public static final String KEY_POSITION_TARGET = "KEY_POSITION_TARGET";

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
        SEEK_SONG,
        SHUFFLE_SONG,
        REPEAT_SONG,

        ADD_SONG,
        SWAP_SONG,

    }

    public enum Mode {
        NORMAL,     // run to end of list song and stop
        SHUFFLE,    // shuffle list song and run first song (run like normal)
        REPEAT,     // run to end of list song and restart
        REPEAT_ONE, // run to end of song and restart

    }

}
