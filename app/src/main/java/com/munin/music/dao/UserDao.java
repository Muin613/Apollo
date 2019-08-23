package com.munin.music.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
@Dao
public interface UserDao extends BaseDao<User> {
    //如果是查询

    @Query("SELECT * FROM User")
    public Flowable<List<User>> getAll();

    @Query("SELECT * FROM User WHERE id IN (:ids)")
    public LiveData<List<User>> loadAllByIds(int[] ids);

    @Query("SELECT * FROM User WHERE :userId LIKE :userId")
    public Cursor findByUserId(String userId);

}