package com.cfox.greendaomultdb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cfox.greendaomultdb.db.dao.DaoSession;
import com.cfox.greendaomultdb.db.manager.DatabaseDaoManager;
import com.cfox.greendaomultdb.db.manager.DatabasePath;
import com.cfox.greendaomultdb.db.table.TableAa;
import com.cfox.greendaomultdb.db.table.TableAb;
import com.cfox.greendaomultdb.db.table.TableBa;
import com.cfox.greendaomultdb.db.table.TableBb;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    DaoSession mSessionA, mSessionB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSessionA = DatabaseDaoManager.getDaoSesson(DatabasePath.DB_A_NAME);
        mSessionB = DatabaseDaoManager.getDaoSesson(DatabasePath.DB_B_NAME);
    }

    public void insert(View view) {
        TableAa aa = new TableAa();
        aa.setName("aa");
        aa.setAge(23);

        TableAb ab = new TableAb();
        ab.setName("ab");

        mSessionA.getTableAaDao().insertInTx(aa);
        mSessionA.getTableAbDao().insertInTx(ab);

        TableBa ba = new TableBa();
        ba.setName("ba");
        ba.setAge(44);
        TableBb bb = new TableBb();
        bb.setName("bb");

        mSessionB.getTableBaDao().insertInTx(ba);
        mSessionB.getTableBbDao().insertInTx(bb);
    }

    public void query(View view) {

        List<TableAa> aaList = mSessionA.getTableAaDao().loadAll();
        for (TableAa aa : aaList) {
            Log.d(TAG, "query: aa : name:" + aa.getName());
        }

    }
}
