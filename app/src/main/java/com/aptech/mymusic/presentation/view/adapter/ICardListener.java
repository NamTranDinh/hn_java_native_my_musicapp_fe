package com.aptech.mymusic.presentation.view.adapter;

import com.aptech.mymusic.domain.entity.CardModel;

public interface ICardListener {

    enum Action {
        SHOW_MODAL, PLAY
    }

    void onCardClicked(CardModel model, Action action);

}
