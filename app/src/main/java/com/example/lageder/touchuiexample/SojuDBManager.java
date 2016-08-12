package com.example.lageder.touchuiexample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by Lageder on 2016-08-03.
 */
public class SojuDBManager extends SQLiteOpenHelper {

    public SojuDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블을 생성한다.
        // create table 테이블명 (컬럼명 타입 옵션);
        db.execSQL("CREATE TABLE IF NOT EXISTS SOJU ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "sqltime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                "C1 integer default 0,"+
                "GOOD_DAY integer default 0,"+
                "SOON_HARI integer default 0,"+
                "LIKE_FIRST integer default 0,"+
                "MT_HANRA integer default 0"+
                " );");
    }
    // IF NOT EXISTS

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public SOOL[] SortData() {
        SQLiteDatabase db = getReadableDatabase();
        SOOL[] sools = new SOOL[5];

        Cursor cursor = db.rawQuery("select * from (select " +
                "sum(SOJU.C1) C1, " +
                "sum(SOJU.GOOD_DAY) GOOD_DAY, " +
                "sum(SOJU.LIKE_FIRST) LIKE_FIRST," +
                "sum(SOJU.SOON_HARI) SOON_HARI," +
                "sum(SOJU.MT_HANRA) MT_HANRA " +
                "from SOJU " +
                "where (SOJU.sqltime > '2016-07-05 00:00:00') " +
                "and (SOJU.sqltime < '2016-08-05 00:00:00'))", null);
        while(cursor.moveToNext()) {

            sools[0] = new SOOL("C1",cursor.getInt(0));
            sools[1] = new SOOL("GOOD_DAY",cursor.getInt(1));
            sools[2] = new SOOL("LIKE_FIRST",cursor.getInt(2));
            sools[3] = new SOOL("SOON_HARI",cursor.getInt(3));
            sools[4] = new SOOL("MT_HANRA",cursor.getInt(4));

            Arrays.sort(sools);


/*            str = cursor.getInt(0) + " is C1 and "
            + cursor.getInt(1) + " is GOOD_DAY";
            Log.e("DB",str);
            str = cursor.getInt(2) + " is Like First and "
                    + cursor.getInt(3) + " is SOON_HARI and ";
            Log.e("DB",str);
            str = cursor.getInt(4) + " is MT_HANRA";
            Log.e("DB",str);*/
        }
        return sools;
    }
}
