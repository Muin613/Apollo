package com.munin.music.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
    private String userName = "";
    private String password = "";
    @Unique
    private String userId = "";

    @Generated(hash = 294648399)
    public User(String userName, String password, String userId) {
        this.userName = userName;
        this.password = password;
        this.userId = userId;
    }

    @Generated(hash = 586692638)
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

}
