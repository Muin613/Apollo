package com.munin.music.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.munin.library.database.DatabaseDelegate;
import com.munin.library.database.SqlScriptUtils;
import com.munin.library.log.Logger;
import com.munin.music.dao.AppDatabase;

import static com.munin.library.utils.ContextUtils.getApplicationContext;


/**
 * @author M
 */
public class DataBaseUtils {
    private static AppDatabase mAppDatabase;

    public static void initGreenDao(Context context) {
        mAppDatabase = Room.databaseBuilder(context, AppDatabase.class, "muin-db")
                .allowMainThreadQueries()
                .addMigrations(new Migration(1,2) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        SqlScriptUtils.execute(new DatabaseDelegate() {
                            @Override
                            public void execSQL(String sql) {
                                database.execSQL(sql);
                            }
                        }, "2.sql");
                    }
                })
                .build();
    }

    public static AppDatabase getDatabase() {
        return mAppDatabase;
    }

//    public static class CustomDBHelper extends DaoMaster.OpenHelper {
//        private static final String TAG = "CustomDBHelper";
//
//        public CustomDBHelper(Context context, String name) {
//            super(context, name);
//        }
//
//        @Override
//        public void onUpgrade(Database db, int oldVersion, int newVersion) {
//            super.onUpgrade(db, oldVersion, newVersion);
//            Logger.i(TAG, "onUpgrade: oldVersion = " + oldVersion);
//            if (oldVersion <= 1) {
//                SqlScriptUtils.execute(db, "2.sql");
//            }
//        }
//    }
}
