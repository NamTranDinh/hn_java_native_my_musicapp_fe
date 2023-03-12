package com.aptech.mymusic.presentation.view.service;

import com.aptech.mymusic.di.DataInjection;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.utils.MusicPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicBundle {

    private static MusicBundle instance;
    MusicDelegate.Mode mMode;
    SongModel mSong;
    List<SongModel> mListSongOrigin;
    List<SongModel> mListSongTemp;
    private MusicBundle() {
        this.mMode = getPreference().getLastMode();
        this.mSong = getPreference().getLastSong();
        this.mListSongOrigin = getPreference().getLastListSong();
        this.mListSongTemp = new ArrayList<>(this.mListSongOrigin);
    }

    public static MusicBundle getInstance() {
        if (instance == null) {
            instance = new MusicBundle();
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
            if (mMode == MusicDelegate.Mode.SHUFFLE) {
                Collections.swap(mListSongTemp, drag, target);
            } else {
                Collections.swap(mListSongOrigin, drag, target);
                Collections.swap(mListSongTemp, drag, target);
                getPreference().setLastListSong(mListSongOrigin);
            }
        } catch (Throwable ignore) {
        }
    }

    MusicPreference getPreference() {
        return DataInjection.provideMusicPreference();
    }

    private int validateSongIndex(int index, int size) {
        return index >= 0 && index <= size ? index : size;
    }

}
