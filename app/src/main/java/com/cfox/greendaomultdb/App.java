package com.cfox.greendaomultdb;

import android.app.Application;

import com.cfox.greendaomultdb.db.manager.DatabaseDaoManager;
import com.cfox.greendaomultdb.db.manager.DatabasePath;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DatabasePath.initDatabasePath();
        initGreenDao();
    }

    private void initGreenDao() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        DatabaseDaoManager.initSessions(this);
    }


    @Override
    public File getDatabasePath(String name) {
        File dbFile = DatabasePath.getDatabasePath(name);
        if (dbFile != null) {
            return dbFile;
        }
        return super.getDatabasePath(name);
    }
}
