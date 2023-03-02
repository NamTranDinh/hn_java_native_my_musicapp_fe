package com.aptech.mymusic.domain.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel extends CardModel {

    @SerializedName("theme_id")
    @Expose
    private String themeId;

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

}
