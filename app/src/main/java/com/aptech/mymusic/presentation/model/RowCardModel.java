package com.aptech.mymusic.presentation.model;

import com.aptech.mymusic.domain.entity.CardModel;

import java.util.List;

public class RowCardModel {

    private String title;
    private List<CardModel> cardModelList;

    public RowCardModel(String title, List<CardModel> cardModelList) {
        this.title = title;
        this.cardModelList = cardModelList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CardModel> getCardModelList() {
        return cardModelList;
    }

    public void setCardModelList(List<CardModel> cardModelList) {
        this.cardModelList = cardModelList;
    }
}
