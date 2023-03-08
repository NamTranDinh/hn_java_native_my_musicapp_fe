package com.aptech.mymusic.presentation.view.activity;

import com.aptech.mymusic.domain.entity.CardModel;
import com.aptech.mymusic.domain.entity.SongModel;

import java.util.List;

public interface ISendDataToDetail {

    enum Action {
        SHOW_MODAL, PLAY
    }

    void sendDataListener(CardModel model, Action action);

    void sendDataListener(List<SongModel> songs, int index);

}
