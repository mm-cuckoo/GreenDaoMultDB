package com.cfox.greendaomultdb.db.manager.base;

import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatabasePathController {
    private static final String TAG = "DatabasePathController";

    private static Map<String, String> dataPathMap = new HashMap<String, String>();


    public static void setDatabasePath(String filePath, String name) {
        dataPathMap.put(name, filePath);
    }

    public static File getDatabasePath(String name) {
        return createFile(dataPathMap.get(name), name);
    }

    public static Set<String> getDatabasePathNames() {
        return dataPathMap.keySet();
    }

    private static File createFile(String filePath, String name) {
        if (filePath == null || name == null) {
            return null;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e(TAG, "create db file error...... , check permission WRITE_EXTERNAL_STORAGE");
                return null;
            }
        }

        return new File(filePath, name);
    }
}
