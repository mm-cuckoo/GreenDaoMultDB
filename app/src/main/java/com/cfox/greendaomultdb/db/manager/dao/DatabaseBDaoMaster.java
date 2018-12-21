package com.cfox.greendaomultdb.db.manager.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cfox.greendaomultdb.db.dao.DaoMaster;
import com.cfox.greendaomultdb.db.dao.TableBaDao;
import com.cfox.greendaomultdb.db.dao.TableBbDao;
import com.cfox.greendaomultdb.db.manager.base.MigrationHelper;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

public class DatabaseBDaoMaster extends DaoMaster {
    public DatabaseBDaoMaster(SQLiteDatabase db) {
        super(db);
    }

    public DatabaseBDaoMaster(Database db) {
        super(db);
    }

    public static void createAllTables(Database db, boolean ifNotExists) {
        TableBaDao.createTable(db, ifNotExists);
        TableBbDao.createTable(db, ifNotExists);

    }

    public static void dropAllTables(Database db, boolean ifExists) {
        TableBaDao.dropTable(db, ifExists);
        TableBbDao.dropTable(db, ifExists);
    }

    public static  class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            createAllTables(db, false);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {

            MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
                @Override
                public void onCreateAllTables(Database db, boolean ifNotExists) {
                    createAllTables(db, ifNotExists);
                }

                @Override
                public void onDropAllTables(Database db, boolean ifExists) {
                    dropAllTables(db, ifExists);

                }
            },TableBaDao.class, TableBaDao.class);

        }
    }
}
