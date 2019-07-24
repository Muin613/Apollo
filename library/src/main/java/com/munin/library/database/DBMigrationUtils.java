package com.munin.library.database;

import com.munin.library.log.Logger;
import com.munin.library.utils.ContextUtils;
import com.munin.library.utils.IOUtils;
import com.munin.library.utils.StringUtils;

import org.greenrobot.greendao.database.Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author M
 */
public class DBMigrationUtils {
    private static final String TAG = "DBMigrationUtils";

    public static void update(Database db, String sqlName) {
        if (db == null) {
            Logger.i(TAG, "update: db is null!");
            return;
        }
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;

        BufferedReader bufferedReader = null;
        try {
            StringBuffer sql = new StringBuffer();
            inputStream = ContextUtils.getApplicationContext().getAssets().open("sql" + File.separator + sqlName);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                Logger.i(TAG, "data :" + line);
                if (line.startsWith("<--") || line.endsWith("-->") || StringUtils.isBlank(line)) {
                    continue;
                }
                sql.append(line);
            }
            Logger.i(TAG, "update: StringBuffer =" + sql.toString());
            String[] result = sql.toString().split(";");
            for (String data : result) {
                db.execSQL(data);
            }
        } catch (Exception e) {
            Logger.i(TAG, "update: error! " + e.getMessage());
        } finally {
            IOUtils.close(inputStream);
            IOUtils.close(inputStreamReader);
            IOUtils.close(bufferedReader);
        }
    }
}
