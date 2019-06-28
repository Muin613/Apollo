package com.munin.music.data;


import java.io.Serializable;

public class MusicData implements Serializable {
    /*音乐资源id*/
    private int mMusicRes;
    private String mMusicUrl;
    /*音乐名称*/
    private String mMusicName;
    /*作者*/
    private String mMusicAuthor;

    public MusicData(){}
    public MusicData(int mMusicRes, String mMusicUrl, String mMusicName, String mMusicAuthor) {
        this.mMusicRes = mMusicRes;
        this.mMusicUrl = mMusicUrl;
        this.mMusicName = mMusicName;
        this.mMusicAuthor = mMusicAuthor;
    }

    public int getMusicRes() {
        return mMusicRes;
    }

    public String  getMusicUrl() {
        return mMusicUrl;
    }

    public String getMusicName() {
        return mMusicName;
    }

    public String getMusicAuthor() {
        return mMusicAuthor;
    }

    public void setmMusicRes(int mMusicRes) {
        this.mMusicRes = mMusicRes;
    }

    public void setMusicUrl(String mMusicUrl) {
        this.mMusicUrl = mMusicUrl;
    }

    public void setmMusicName(String mMusicName) {
        this.mMusicName = mMusicName;
    }

    public void setmMusicAuthor(String mMusicAuthor) {
        this.mMusicAuthor = mMusicAuthor;
    }
}