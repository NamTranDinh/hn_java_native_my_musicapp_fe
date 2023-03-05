package com.aptech.mymusic.domain.entity;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class CategoryModel implements CardModel {

    @SerializedName("id")
    private Integer id;
    @SerializedName("topicIds")
    private String topicIds;
    @SerializedName("name")
    private String name;
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

    public String getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(String topicIds) {
        this.topicIds = topicIds;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "CategoryModel{" +
                "id=" + id +
                ", topicIds='" + topicIds + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}