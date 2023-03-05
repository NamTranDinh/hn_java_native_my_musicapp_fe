package com.aptech.mymusic.domain.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class PlaylistModel implements CardModel {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("imgIcon")
    private String imgIcon;
    @SerializedName("imgBackground")
    private String imgBackground;
    @SerializedName("imageIconUrl")
    private String imageIconUrl;
    @SerializedName("imageBackgroundUrl")
    private String imageBackgroundUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getImageUrl() {
        return getImageIconUrl();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(String imgIcon) {
        this.imgIcon = imgIcon;
    }

    public String getImgBackground() {
        return imgBackground;
    }

    public void setImgBackground(String imgBackground) {
        this.imgBackground = imgBackground;
    }

    public String getImageIconUrl() {
        return imageIconUrl;
    }

    public void setImageIconUrl(String imageIconUrl) {
        this.imageIconUrl = imageIconUrl;
    }

    public String getImageBackgroundUrl() {
        return imageBackgroundUrl;
    }

    public void setImageBackgroundUrl(String imageBackgroundUrl) {
        this.imageBackgroundUrl = imageBackgroundUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaylistModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imgIcon='" + imgIcon + '\'' +
                ", imgBackground='" + imgBackground + '\'' +
                ", imageIconUrl='" + imageIconUrl + '\'' +
                ", imageBackgroundUrl='" + imageBackgroundUrl + '\'' +
                '}';
    }
}

