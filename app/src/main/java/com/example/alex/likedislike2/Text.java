package com.example.alex.likedislike2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Text {
    private String mText;

    @SerializedName("id")
    @Expose
    private String textId;

    public Text(String textId,String text) {
        this.mText = text;
        this.textId = textId;
    }

    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    public Text(String text) {
        this.mText = text;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

}
