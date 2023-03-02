package com.aptech.mymusic.domain.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumModel extends CardModel {

    @SerializedName("singer_name")
    @Expose
    private String singerName;

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

}
