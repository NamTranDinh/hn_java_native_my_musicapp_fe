package com.aptech.mymusic.domain.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class AlbumModel implements CardModel {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("singerName")
    private String singerName;
    @SerializedName("image")
    private String image;
    @SerializedName("imageUrl")
    private String imageUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "AlbumModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", singerName='" + singerName + '\'' +
                ", image='" + image + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
