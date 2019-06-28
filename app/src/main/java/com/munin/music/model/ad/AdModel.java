package com.munin.music.model.ad;

import com.munin.music.ad.controller.base.AdType;

/**
 * @author M
 */
public class AdModel {
    private @AdType
    String mType;
    private String mAdUrl;
    private String mBeginTime;
    private String mEndTime;
    private String mShowTime;

    public AdModel() {
    }

    public @AdType
    String getType() {
        return mType;
    }

    public void setType(@AdType String type) {
        this.mType = type;
    }

    public String getAdUrl() {
        return mAdUrl;
    }

    public void setAdUrl(String adUrl) {
        this.mAdUrl = adUrl;
    }

    public String getBeginTime() {
        return mBeginTime;
    }

    public void setBeginTime(String beginTime) {
        this.mBeginTime = beginTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        this.mEndTime = endTime;
    }

    public String getShowTime() {
        return mShowTime;
    }

    public void setShowTime(String showTime) {
        this.mShowTime = showTime;
    }
}
