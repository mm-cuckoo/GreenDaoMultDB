package com.cfox.greendaomultdb.db.manager;

import android.content.Context;

import com.cfox.greendaomultdb.db.dao.DaoSession;
import com.cfox.greendaomultdb.db.manager.base.DatabasePathController;
import com.cfox.greendaomultdb.db.manager.dao.DatabaseADaoMaster;
import com.cfox.greendaomultdb.db.manager.dao.DatabaseBDaoMaster;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatabaseDaoManager {
    private static Map<String ,DaoSession> sessionMap = new HashMap<>();
    public static void initSessions(Context context) {
        Set<String> names = DatabasePathController.getDatabasePathNames();
        for (String name : names) {
            switch (name) {
                case DatabasePath.DB_A_NAME:
                    DatabaseADaoMaster.OpenHelper aHelper = new DatabaseADaoMaster.OpenHelper(context, name);
                    DatabaseADaoMaster aMaster = new DatabaseADaoMaster(aHelper.getEncryptedWritableDb("hello"));
                    sessionMap.put(name, aMaster.newSession());
                    break;

                case DatabasePath.DB_B_NAME:
                    DatabaseBDaoMaster.OpenHelper bHelper = new DatabaseBDaoMaster.OpenHelper(context, name);
                    DatabaseBDaoMaster bMaster = new DatabaseBDaoMaster(bHelper.getWritableDb());
                    sessionMap.put(name, bMaster.newSession());
                    break;
            }
        }
    }

    public static DaoSession getDaoSesson(String name) {
        return sessionMap.get(name);
    }


}
