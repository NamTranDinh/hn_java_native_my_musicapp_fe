package com.aptech.mymusic.presentation.view.service;

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

}
