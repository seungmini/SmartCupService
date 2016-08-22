package com.example.lageder.touchuiexample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;

/**
 * Created by LeeSeungMin on 2016-08-14.
 */
public class MakgeolliDBManager extends SQLiteOpenHelper {

    public MakgeolliDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블을 생성한다.
        // create table 테이블명 (컬럼명 타입 옵션);
        db.execSQL("CREATE TABLE IF NOT EXISTS MAKG ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "sqltime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                "GUKS integer default 0,"+
                "GEUM integer default 0,"+
                "NEUR integer default 0,"+
                "SAEN integer default 0,"+
                "SEOU integer default 0,"+
                "UGUK integer default 0,"+
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

    public SOOL[] printData() {
        SQLiteDatabase db = getReadableDatabase();
        SOOL[] sools = new SOOL[5];

        Cursor cursor = db.rawQuery("select * from (select " +
                "sum(MAKG.GUKS) GUKS, " +
                "sum(MAKG.GEUM) GEUM, " +
                "sum(MAKG.NEUR) NEUR," +
                "sum(MAKG.SAEN) SAEN," +
                "sum(MAKG.SEOU) SEOU," +
                "sum(MAKG.UGUK) UGUK," +
                "from MAKG " +
                "where (MAKG.sqltime > '2016-07-05 00:00:00') " +
                "and (MAKG.sqltime < '2016-08-05 00:00:00'))", null);
        while(cursor.moveToNext()) {

            sools[0] = new SOOL("GUKS",cursor.getInt(0));
            sools[1] = new SOOL("GEUM",cursor.getInt(1));
            sools[2] = new SOOL("NEUR",cursor.getInt(2));
            sools[3] = new SOOL("SAEN",cursor.getInt(3));
            sools[3] = new SOOL("SEOU",cursor.getInt(4));
            sools[3] = new SOOL("UGUK",cursor.getInt(5));

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
