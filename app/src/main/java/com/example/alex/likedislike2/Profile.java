package com.example.alex.likedislike2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("src")
    @Expose
    private String imageUrl;

    @SerializedName("id")
    @Expose
    private String imageId;

    public Profile(String imageUrl, String imageId) {
        this.imageUrl = imageUrl;
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

}
