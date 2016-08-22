package com.example.lageder.touchuiexample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        /*db.execSQL("CREATE TABLE IF NOT EXISTS SOJU ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "sqltime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                "C1 integer default 0,"+
                "GOOD_DAY integer default 0,"+
                "SOON_HARI integer default 0,"+
                "LIKE_FIRST integer default 0,"+
                "MT_HANRA integer default 0"+
                " );");

        db.execSQL("CREATE TABLE IF NOT EXISTS BEER ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "sqltime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                "CASS integer default 0,"+
                "HITE integer default 0,"+
                "MAX integer default 0,"+
                "OB integer default 0"+
                " );");


        db.execSQL("CREATE TABLE IF NOT EXISTS MAKG ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "sqltime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                "GUKS integer default 0,"+
                "GEUM integer default 0,"+
                "NEUR integer default 0,"+
                "SAEN integer default 0,"+
                "SEOU integer default 0,"+
                "UGUK integer default 0"+
                " );");
*/
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

    public void getMostTime(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        int[] count = new int[29];
        for(int i = 0 ; i < 29;i++){
            count[i] = 0;
        }
        String query = "select strftime('%H',s_time),strftime('%H',f_time) from CUP";

        cs = db.rawQuery(query,null);
        while(cs.moveToNext()) {
            int s_time = cs.getInt(0);
            int f_time = cs.getInt(1);
            if(f_time < 5){
                f_time = f_time + 24;
            }
            /*
            for(int time = s_time; time <= f_time; time++){
                count[time]++;
            }*/
            count[s_time]++;
            //Log.e(""," " + s_time + " // " + f_time);
        }

        for(int i = 0 ; i < 29; i++){
            Log.e(""," i : " + i + " count : " + count[i]);
        }

    }


    public String getMostSoju(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        String result="";
        int count_c1,count_gd,count_sh,count_lf,count_mh;
        Map<String, Integer> count_hashmap = new HashMap<String, Integer>();

        String query_c1 = "select * from SOJU where C1 >=1 ";
        String query_gd = "select * from SOJU where GOOD_DAY >=1 ";
        String query_sh = "select * from SOJU where SOON_HARI >=1 ";
        String query_lf = "select * from SOJU where LIKE_FIRST >=1 ";
        String query_mh = "select * from SOJU where MT_HANRA >=1 ";

        cs = db.rawQuery(query_c1,null);
        count_c1 = cs.getCount();
        count_hashmap.put("C1",count_c1);

        cs = db.rawQuery(query_gd,null);
        count_gd = cs.getCount();
        count_hashmap.put("좋은데이",count_gd);


        cs = db.rawQuery(query_sh,null);
        count_sh = cs.getCount();
        count_hashmap.put("순하리",count_sh);


        cs = db.rawQuery(query_lf,null);
        count_lf = cs.getCount();
        count_hashmap.put("처음처럼",count_lf);


        cs= db.rawQuery(query_mh,null);
        count_mh = cs.getCount();
        count_hashmap.put("한라산",count_mh);

        int maxValueInMap=(Collections.max(count_hashmap.values()));  // This will return max value in the Hashmap
        for (Map.Entry<String, Integer> entry : count_hashmap.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==maxValueInMap) {
               result = entry.getKey();     // Print the key with max value
            }
        }

        return result;
    }


    public String getMostBeer(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        String result="";
        int count_cass,count_hite,count_max,count_ob;
        Map<String, Integer> count_hashmap = new HashMap<String, Integer>();

        String query_cass = "select * from BEER where CASS >=1 ";
        String query_hite = "select * from BEER where HITE >=1 ";
        String query_max = "select * from BEER where MAX >=1 ";
        String query_ob = "select * from BEER where OB >=1 ";

        cs = db.rawQuery(query_cass,null);
        count_cass = cs.getCount();
        count_hashmap.put("Cass",count_cass);

        cs = db.rawQuery(query_hite,null);
        count_hite = cs.getCount();
        count_hashmap.put("Hite",count_hite);


        cs = db.rawQuery(query_max,null);
        count_max = cs.getCount();
        count_hashmap.put("Max",count_max);


        cs = db.rawQuery(query_ob,null);
        count_ob = cs.getCount();
        count_hashmap.put("OB",count_ob);


        int maxValueInMap=(Collections.max(count_hashmap.values()));  // This will return max value in the Hashmap
        for (Map.Entry<String, Integer> entry : count_hashmap.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==maxValueInMap) {
                result = entry.getKey();     // Print the key with max value
            }
        }

        return result;
    }


    public String getMostMakg(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        String result="";
        int count_guks,count_geum,count_neur,count_saen,count_seou,count_uguk;
        Map<String, Integer> count_hashmap = new HashMap<String, Integer>();

        String query_guks = "select * from MAKG where GUKS >=1 ";
        String query_geum = "select * from MAKG where GEUM >=1 ";
        String query_neur = "select * from MAKG where NEUR >=1 ";
        String query_saen = "select * from MAKG where SAEN >=1 ";
        String query_seou = "select * from MAKG where SEOU >=1 ";
        String query_uguk = "select * from MAKG where UGUK >=1 ";


        cs = db.rawQuery(query_guks,null);
        count_guks = cs.getCount();
        count_hashmap.put("국산당쌀 막걸리",count_guks);

        cs = db.rawQuery(query_geum,null);
        count_geum = cs.getCount();
        count_hashmap.put("금정산성 막걸리",count_geum);


        cs = db.rawQuery(query_neur,null);
        count_neur = cs.getCount();
        count_hashmap.put("느루마을",count_neur);


        cs = db.rawQuery(query_saen,null);
        count_saen = cs.getCount();
        count_hashmap.put("생탁",count_saen);


        cs= db.rawQuery(query_seou,null);
        count_seou = cs.getCount();
        count_hashmap.put("서울장수 막걸리",count_seou);


        cs= db.rawQuery(query_uguk,null);
        count_uguk = cs.getCount();
        count_hashmap.put("우국생",count_uguk);


        int maxValueInMap=(Collections.max(count_hashmap.values()));  // This will return max value in the Hashmap
        for (Map.Entry<String, Integer> entry : count_hashmap.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==maxValueInMap) {
                result = entry.getKey();     // Print the key with max value
            }
        }

        return result;
    }

    public String getMostDay(String date, String dateType) throws Exception {

        String day = "" ;

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType) ;
        Date nDate = dateFormat.parse(date) ;

        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;

        switch(dayNum){
            case 1:
                day = "일";
                break ;
            case 2:
                day = "월";
                break ;
            case 3:
                day = "화";
                break ;
            case 4:
                day = "수";
                break ;
            case 5:
                day = "목";
                break ;
            case 6:
                day = "금";
                break ;
            case 7:
                day = "토";
                break ;

        }

        return day ;
    }


}
