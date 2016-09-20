package datas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lageder on 2016-08-03.
 */
public class SCSDBManager extends SQLiteOpenHelper {

    public SCSDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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

    public String getLatestTime(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        String query = "select s_time from CUP order by s_time desc limit 1";

        cs = db.rawQuery(query, null);
        cs.moveToFirst();

        return cs.getString(0);
    }


    public String getMostTime(){
        String result="";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        Map<Integer, Integer> count_time = new HashMap<Integer, Integer>();
        int[] count = new int[29];
        for(int i = 0 ; i < 29;i++){
            count[i] = 0;
        }
        String query_time = "select strftime('%H',s_time),strftime('%H',f_time) from CUP";

        cs = db.rawQuery(query_time,null);
        cs.moveToFirst();
        while(cs.moveToNext()) {
            int s_time = cs.getInt(0);
            int f_time = cs.getInt(1);
            if(f_time < 5){
                f_time = f_time + 24;
            }

            for(int time = s_time; time <= f_time; time++){
                count[time]++;
            }
        }
        for(int i = 0; i < 29; i++){
            count_time.put(i,count[i]);
        }
        int result_time = 0;
        int maxValueInMap_time=(Collections.max(count_time.values()));  // This will return max value in the Hashmap
        for (Map.Entry<Integer, Integer> entry : count_time.entrySet()) {  // Itrate through hashmap
            if (entry.getValue() == maxValueInMap_time) {
                result_time = entry.getKey();     // Print the key with max value
            }
        }
        if(result_time >= 25){
            result_time -= 24;
        }


        Cursor cs_day;
        //String query_day = "select s_time from CUP";
        String query_day = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), strftime('%H',s_time) FROM CUP";

        cs_day = db.rawQuery(query_day,null);
        cs_day.moveToFirst();
        Calendar cal = Calendar.getInstance();
        Map<Integer, Integer> count_day = new HashMap<Integer, Integer>();

        int[] day = new int[8];
        for(int i = 0 ; i < 8; i++){
            day[i] = 0;
        }
        while(cs_day.moveToNext()) {

            cal.set(Calendar.YEAR, Integer.parseInt(cs_day.getString(0)));
            cal.set(Calendar.MONTH, Integer.parseInt(cs_day.getString(1)) - 1);
            cal.set(Calendar.DATE, Integer.parseInt(cs_day.getString(2)));

            int dayNum = cal.get(Calendar.DAY_OF_WEEK);//1 =일요일

            if(cs_day.getInt(3) <=5){
                dayNum--;
                if(dayNum == 0){
                    dayNum = 7;
                }
            }


            day[dayNum]++;
        }
        for(int i = 0; i < 8; i++){
            count_day.put(i,day[i]);
        }
        int result_day_num = 0;
        int maxValueInMap_day=(Collections.max(count_day.values()));  // This will return max value in the Hashmap
        for (Map.Entry<Integer, Integer> entry : count_day.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==maxValueInMap_day) {
                result_day_num = entry.getKey();     // Print the key with max value
            }
        }
        String result_day="";
        switch(result_day_num){
            case 1:
                result_day = "일";
                break;
            case 2:
                result_day = "월";
                break;
            case 3:
                result_day = "화";
                break;
            case 4:
                result_day = "수";
                break;
            case 5:
                result_day = "목";
                break;
            case 6:
                result_day = "금";
                break;
            case 7:
                result_day = "토";
                break;
        }
        result = result_day + "요일, " + result_time + "시";
        return result;
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

    public int getDay(String date) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
        Date nDate = dateFormat.parse(date) ;

        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;

        return dayNum ;
    }



    public ArrayList<IBarDataSet> getDataSet_month() {

        ArrayList<BarEntry> soju_list = new ArrayList<BarEntry>();
        ArrayList<BarEntry> beer_list = new ArrayList<BarEntry>();
        ArrayList<BarEntry> mack_list = new ArrayList<BarEntry>();

        BarDataSet set1, set2, set3;

        Calendar calendar = Calendar.getInstance();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today_date = CurDateFormat.format(date);
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        String query = "";

        float[] ml_soju = new float[5];//일요일 = 1, 토요일 = 7
        float[] ml_beer = new float[5];
        float[] ml_mack = new float[5];

        for (int i = 0; i < 5; i++) {
            ml_soju[i] = 0;
            ml_beer[i] = 0;
            ml_mack[i] = 0;
        }

        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        Calendar cal = Calendar.getInstance();

        for (int num = 1; num < 5; num++) {

            switch (today) {//num = 0 => 이번주, num = 1 -> 1주전 이런식
                case 1://일
                    query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date + " 05:00:00', '-" + (7 * num + 0) + " days') AND datetime('" + today_date + " 05:00:00', '-" + (7 * (num - 1) + 0) + " days');";
                    break;
                case 2:
                    query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date + " 05:00:00', '-" + (7 * num + 1) + " days') AND datetime('" + today_date + " 05:00:00', '-" + (7 * (num - 1) + 1) + " days');";
                    break;
                case 3:
                    query = "SELECT strftime('%Y',s_time),strftime('%m',s_time), strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date + " 05:00:00', '-" + (7 * num + 2) + " days') AND datetime('" + today_date + " 05:00:00', '-" + (7 * (num - 1) + 2) + " days');";
                    break;
                case 4:
                    query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date + " 05:00:00', '-" + (7 * num + 3) + " days') AND datetime('" + today_date + " 05:00:00', '-" + (7 * (num - 1) + 3) + " days');";
                    break;
                case 5:
                    query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date + " 05:00:00', '-" + (7 * num + 4) + " days') AND datetime('" + today_date + " 05:00:00', '-" + (7 * (num - 1) + 4) + " days');";
                    break;
                case 6:
                    query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date + " 05:00:00', '-" + (7 * num + 5) + " days') AND datetime('" + today_date + " 05:00:00', '-" + (7 * (num - 1) + 5) + " days');";
                    break;
                case 7:
                    query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date + " 05:00:00', '-" + (7 * num + 6) + " days') AND datetime('" + today_date + " 05:00:00', '-" + (7 * (num - 1) + 6) + " days');";
                    break;
            }

            cs = db.rawQuery(query, null);


            while (cs.moveToNext()) {

                cal.set(Calendar.YEAR, Integer.parseInt(cs.getString(0)));
                cal.set(Calendar.MONTH, Integer.parseInt(cs.getString(1)) - 1);
                cal.set(Calendar.DATE, Integer.parseInt(cs.getString(2)));

            //    int dayNum = cal.get(Calendar.DAY_OF_WEEK);

                ml_soju[num] += cs.getInt(3);
                ml_beer[num] += cs.getInt(4);
                ml_mack[num] += cs.getInt(5);

            }


        }

        for (int i = 0; i < 4; i++) {
            soju_list.add(new BarEntry(ml_soju[i + 1]/360, i));
            beer_list.add(new BarEntry(ml_beer[i + 1]/500, i));
            mack_list.add(new BarEntry(ml_mack[i + 1]/750, i));
        }

        set1 = new BarDataSet(soju_list, "소주");
        set1.setColor(Color.rgb(36, 166, 9));

        set2 = new BarDataSet(beer_list, "맥주");
        set2.setColor(Color.rgb(242, 168, 30));

        set3 = new BarDataSet(mack_list, "막걸리");
        set3.setColor(Color.rgb(255, 255, 201));

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        return dataSets;

    }

    public ArrayList<IBarDataSet> getDataSet_beforeweek(int num){

        ArrayList<BarEntry> soju_list = new ArrayList<BarEntry>();
        ArrayList<BarEntry> beer_list = new ArrayList<BarEntry>();
        ArrayList<BarEntry> mack_list = new ArrayList<BarEntry>();

        BarDataSet set1, set2, set3;

        Calendar calendar = Calendar.getInstance();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today_date = CurDateFormat.format(date);
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        String query ="";

        switch(today){//num = 0 => 이번주, num = 1 -> 1주전 이런식
            case 1://일
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-"+(7*num + 0)+" days') AND datetime('" + today_date +" 05:00:00', '-"+(7*(num -1) + 0)+" days');";
                break ;
            case 2:
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-"+(7*num + 1)+" days') AND datetime('" + today_date +" 05:00:00', '-"+(7*(num -1) + 1)+" days');";
                break ;
            case 3:
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time), strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-"+(7*num + 2)+" days') AND datetime('" + today_date +" 05:00:00', '-"+(7*(num -1) + 2)+" days');";
                break ;
            case 4:
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-"+(7*num + 3)+" days') AND datetime('" + today_date +" 05:00:00', '-"+(7*(num -1) + 3)+" days');";
                break ;
            case 5:
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-"+(7*num + 4)+" days') AND datetime('" + today_date +" 05:00:00', '-"+(7*(num -1) + 4)+" days');";
                break ;
            case 6:
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-"+(7*num + 5)+" days') AND datetime('" + today_date +" 05:00:00', '-"+(7*(num -1) + 5)+" days');";
                break ;
            case 7:
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-"+(7*num + 6)+" days') AND datetime('" + today_date +" 05:00:00', '-"+(7*(num -1) + 6)+" days');";
                break ;
        }


        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;

        cs = db.rawQuery(query,null);

        float[] ml_soju = new float[8];//일요일 = 1, 토요일 = 7
        float[] ml_beer = new float[8];
        float[] ml_mack = new float[8];

        for(int i = 0 ; i < 8; i++){
            ml_soju[i] = 0;
            ml_beer[i] = 0;
            ml_mack[i] = 0;
        }

        Calendar cal = Calendar.getInstance();

        while(cs.moveToNext()){

            cal.set(Calendar.YEAR, Integer.parseInt(cs.getString(0)));
            cal.set(Calendar.MONTH, Integer.parseInt(cs.getString(1))-1);
            cal.set(Calendar.DATE, Integer.parseInt(cs.getString(2)));

            int dayNum = cal.get(Calendar.DAY_OF_WEEK);

            ml_soju[dayNum] += cs.getInt(3);
            ml_beer[dayNum] += cs.getInt(4);
            ml_mack[dayNum] += cs.getInt(5);

        }

        for(int i = 0 ; i < 7 ; i++){
            soju_list.add(new BarEntry(ml_soju[i+1]/360,i));
            beer_list.add(new BarEntry(ml_beer[i+1]/500,i));
            mack_list.add(new BarEntry(ml_mack[i+1]/750,i));
        }

        set1 = new BarDataSet(soju_list, "소주");
        set1.setColor(Color.rgb(36, 166, 9));

        set2 = new BarDataSet(beer_list, "맥주");
        set2.setColor(Color.rgb(242, 168, 30));

        set3 = new BarDataSet(mack_list, "막걸리");
        set3.setColor(Color.rgb(255, 255, 201));

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        return dataSets;



    }

    public ArrayList<IBarDataSet> getDataSet_thisweek() {

        ArrayList<BarEntry> soju_list = new ArrayList<BarEntry>();
        ArrayList<BarEntry> beer_list = new ArrayList<BarEntry>();
        ArrayList<BarEntry> mack_list = new ArrayList<BarEntry>();

        BarDataSet set1, set2, set3;

        Calendar calendar = Calendar.getInstance();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today_date = CurDateFormat.format(date);
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        String query ="";

        switch(today){
            case 1://일
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '0 days') AND datetime('" + today_date +" 05:00:00', '-0 days');";
                break ;
            case 2:
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-1 days') AND datetime('" + today_date +" 05:00:00', '-0 days');";
                break ;
            case 3:
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time), strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-2 days') AND datetime('" + today_date +" 05:00:00', '-0 days');";
                break ;
            case 4:
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-3 days') AND datetime('" + today_date +" 05:00:00', '-0 days');";
                break ;
            case 5:
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-4 days') AND datetime('" + today_date +" 05:00:00', '-0 days');";
                break ;
            case 6:
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-5 days') AND datetime('" + today_date +" 05:00:00', '-0 days');";
                break ;
            case 7:
                query = "SELECT strftime('%Y',s_time),strftime('%m',s_time),strftime('%d',s_time), ml_soju, ml_macj, ml_mack FROM CUP WHERE s_time BETWEEN datetime('" + today_date +" 05:00:00', '-6 days') AND datetime('" + today_date +" 05:00:00', '-0 days');";
                break ;
        }


        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;

        cs = db.rawQuery(query,null);

        float[] ml_soju = new float[8];//일요일 = 1, 토요일 = 7
        float[] ml_beer = new float[8];
        float[] ml_mack = new float[8];

        for(int i = 0 ; i < 8; i++){
            ml_soju[i] = 0;
            ml_beer[i] = 0;
            ml_mack[i] = 0;
        }

        Calendar cal = Calendar.getInstance();

        while(cs.moveToNext()){

            cal.set(Calendar.YEAR, Integer.parseInt(cs.getString(0)));
            cal.set(Calendar.MONTH, Integer.parseInt(cs.getString(1))-1);
            cal.set(Calendar.DATE, Integer.parseInt(cs.getString(2)));

            int dayNum = cal.get(Calendar.DAY_OF_WEEK);

            ml_soju[dayNum] += cs.getInt(3);
            ml_beer[dayNum] += cs.getInt(4);
            ml_mack[dayNum] += cs.getInt(5);

        }

        for(int i = 0 ; i < 7 ; i++){
            soju_list.add(new BarEntry(ml_soju[i+1]/360,i));
            beer_list.add(new BarEntry(ml_beer[i+1]/500,i));
            mack_list.add(new BarEntry(ml_mack[i+1]/750,i));
        }

        set1 = new BarDataSet(soju_list, "소주");
        set1.setColor(Color.rgb(36, 166, 9));

        set2 = new BarDataSet(beer_list, "맥주");
        set2.setColor(Color.rgb(242, 168, 30));

        set3 = new BarDataSet(mack_list, "막걸리");
        set3.setColor(Color.rgb(255, 255, 201));

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        return dataSets;

    }

}
