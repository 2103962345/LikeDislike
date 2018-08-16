package com.example.alex.likedislike2;

public class LikeVariable {
private  int mLike =0;
private  ChangeListener listener;
public  int getLike(){
    return mLike;
}
public void setLike(int like){
    mLike=like;
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
    }
}
