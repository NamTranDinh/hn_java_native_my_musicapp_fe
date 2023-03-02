package com.aptech.mymusic.domain.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaylistModel extends CardModel {

    @SerializedName("img_background")
    @Expose
    private String imgBackground;

    public String getImgBackground() {
        return imgBackground;
    }

    public void setImgBackground(String imgBackground) {
        this.imgBackground = imgBackground;
    }

}
