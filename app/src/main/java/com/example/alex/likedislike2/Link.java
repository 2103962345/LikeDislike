package com.example.alex.likedislike2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Link {

    @SerializedName("link")
    @Expose
    private String mLink;

    public Link(String link) {
        this.mLink = link;
    }
    private ChangeListener listener;
    public  String getLink(){
        return mLink;
    }
    public void setLink(String link){
        mLink=link;
        if(listener!=null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }
    public interface ChangeListener{
        void onChange();
    }}
