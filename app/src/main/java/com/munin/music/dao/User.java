package com.munin.music.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;
import java.util.Observable;

import io.reactivex.Flowable;

//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Unique;
//import org.greenrobot.greendao.annotation.Generated;
//
@Entity(tableName = "User")
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id;
    @ColumnInfo(name = "userName")
    private String userName = "";
    @ColumnInfo(name = "password")
    private String password = "";
    //    @Unique
    @ColumnInfo(name = "userId")
    private String userId = "";
    @ColumnInfo(name = "HEADER_IMG")
    private String headerImage = "";
    //    @Generated(hash = 294648399)
    public User(String userName, String password, String userId) {
        this.userName = userName;
        this.password = password;
        this.userId = userId;
    }

    //    @Generated(hash = 586692638)
    public User() {
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }
}


