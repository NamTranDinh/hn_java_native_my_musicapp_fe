package com.aptech.mymusic.domain.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SongModel implements CardModel {

    @SerializedName("id")
    private Integer id;
    @SerializedName("albumIds")
    private String albumIds;
    @SerializedName("categoryIds")
    private String categoryIds;
    @SerializedName("playlistIds")
    private String playlistIds;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("singerName")
    private String singerName;
    @SerializedName("audio")
    private String audio;
    @SerializedName("likes")
    private Integer likes;
    @SerializedName("status")
    private Integer status;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("audioUrl")
    private String audioUrl;
    @Expose
    private boolean isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlbumIds() {
        return albumIds;
    }

    public void setAlbumIds(String albumIds) {
        this.albumIds = albumIds;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getPlaylistIds() {
        return playlistIds;
    }

    public void setPlaylistIds(String playlistIds) {
        this.playlistIds = playlistIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SongModel)) return false;

        SongModel songModel = (SongModel) o;

        if (getId() != null ? !getId().equals(songModel.getId()) : songModel.getId() != null)
            return false;
        if (getName() != null ? !getName().equals(songModel.getName()) : songModel.getName() != null)
            return false;
        return getSingerName() != null ? getSingerName().equals(songModel.getSingerName()) : songModel.getSingerName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getSingerName() != null ? getSingerName().hashCode() : 0);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "SongModel{" +
                "id=" + id +
                ", albumIds='" + albumIds + '\'' +
                ", categoryIds='" + categoryIds + '\'' +
                ", playlistIds='" + playlistIds + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", singerName='" + singerName + '\'' +
                ", audio='" + audio + '\'' +
                ", likes=" + likes +
                ", status=" + status +
                ", imageUrl='" + imageUrl + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                '}';
    }
}