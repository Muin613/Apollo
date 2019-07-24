package com.munin.music.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.munin.library.database.DBMigrationUtils;
import com.munin.music.dao.DaoMaster;
import com.munin.music.dao.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * @author M
 */
public class GreenDaoUtils {
    private static DaoSession mDaoSession;

    public static void initGreenDao(Context context) {
        //创建数据库
        CustomDBHelper helper = new CustomDBHelper(context, "what.db");
        //获取可写数据库
        SQLiteDatabase database = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(database);
        //获取Dao对象管理者
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

    public static class CustomDBHelper extends DaoMaster.OpenHelper {
        public CustomDBHelper(Context context, String name) {
            super(context, name);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            super.onUpgrade(db, oldVersion, newVersion);
            DBMigrationUtils.update(db, "1.sql");
        }
    }
}
