package com.cfox.greendaomultdb.db.manager;

import android.os.Environment;


import com.cfox.greendaomultdb.db.manager.base.DatabasePathController;

import java.io.File;

public class DatabasePath {

    private final static  String BASE_PATH = Environment.getExternalStorageDirectory().getPath();
    public final static String DB_A_PATH = BASE_PATH + "/GreenDaoMult/DBA/";
    public final static String DB_A_NAME = "db_a.db";
    public final static String DB_B_PATH = BASE_PATH + "/GreenDaoMult/DBB/";
    public final static String DB_B_NAME = "db_b.db";

    public static void initDatabasePath() {
        DatabasePathController.setDatabasePath(DB_A_PATH, DB_A_NAME);
        DatabasePathController.setDatabasePath(DB_B_PATH, DB_B_NAME);
    }

    public static File getDatabasePath(String name) {
        return DatabasePathController.getDatabasePath(name);
    }
}
