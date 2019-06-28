package com.munin.music.model.music;

/**
 * @author M
 * 播放数据类
 */
public class MusicModel {
    public final static String MUSIC_BG_IMG_URL_KEY = "music_bg_img_key";
    public final static String MUSIC_MEDIA_URL_KEY = "music_media_url_key";
    public final static String MUSIC_MEDIA_ID_KEY = "music_media_id_key";
    public final static String MUSIC_MEDIA_TITLE_KEY = "music_media_title_key";
    public final static String MUSIC_MEDIA_AUTHOR_KEY = "music_media_author_key";
    private String mImgUrl;
    private String mMusicUrl;
    private String mAuthor;
    private String mTitle;
    private String mId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MusicModel that = (MusicModel) o;
        if (mImgUrl != null ? !mImgUrl.equals(that.getImgUrl()) : that.getImgUrl() != null) {
            return false;
        }
        if (mMusicUrl != null ? !mMusicUrl.equals(that.getMusicUrl()) : that.getMusicUrl() != null) {
            return false;
        }
        if (mAuthor != null ? !mAuthor.equals(that.getAuthor()) : that.getAuthor() != null) {
            return false;
        }
        if (mTitle != null ? !mTitle.equals(that.getTitle()) : that.getTitle() != null) {
            return false;
        }
        return mId != null ? !mId.equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        int result = mImgUrl != null ? mImgUrl.hashCode() : 0;
        result = 31 * result + (mMusicUrl != null ? mMusicUrl.hashCode() : 0);
        result = 31 * result + (mAuthor != null ? mAuthor.hashCode() : 0);
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mId != null ? mId.hashCode() : 0);
        return result;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.mImgUrl = imgUrl;
    }

    public String getMusicUrl() {
        return mMusicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.mMusicUrl = musicUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    @Override
    public String toString() {
        return "MusicModel{" +
                "mImgUrl='" + mImgUrl + '\'' +
                ", mMusicUrl='" + mMusicUrl + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mId='" + mId + '\'' +
                '}';
    }
}
