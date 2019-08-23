package com.munin.music.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BaseDao<T> {
    @Insert
   public void insertItem(T item);//插入单条数据

    @Insert
    public void insertItems(List<T> items);//插入list数据

    @Delete
    public void deleteItem(T item);//删除item

    @Update
    public  void updateItem(T item);//更新item
}
