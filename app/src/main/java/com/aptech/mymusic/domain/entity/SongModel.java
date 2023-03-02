package com.aptech.mymusic.domain.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SongModel extends CardModel {

    @SerializedName("album_ids")
    @Expose
    private String albumId;
    @SerializedName("category_ids")
    @Expose
    private String categoryId;
    @SerializedName("playlist_ids")
    @Expose
    private String playlistId;
    @SerializedName("singer_name")
    @Expose
    private String singerName;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("status")
    @Expose
    private String status;

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return "SongModel{" +
                "id='" + getId() + '\'' +
                "albumId='" + albumId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", playlistId='" + playlistId + '\'' +
                ", singerName='" + singerName + '\'' +
                ", audio='" + audio + '\'' +
                ", likes='" + likes + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
