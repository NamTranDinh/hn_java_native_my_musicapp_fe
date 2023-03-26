package com.aptech.mymusic.domain.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class AdsModel implements CardModel {

    @SerializedName("id")
    private Integer id;
    @SerializedName("song")
    private SongModel song;
    @SerializedName("content")
    private String content;
    @SerializedName("image")
    private String image;
    @SerializedName("status")
    private Enums.Status status;

    @SerializedName("imageUrl")
    private String imageUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SongModel getSong() {
        return song;
    }

    public void setSong(SongModel song) {
        this.song = song;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Enums.Status getStatus() {
        return status;
    }

    public void setStatus(Enums.Status status) {
        this.status = status;
    }

    @Override
    public String getName() {
        return song != null ? song.getName() : null;
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
        return "AdsModel{" +
                "id=" + id +
                ", song=" + song +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", status=" + status +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}