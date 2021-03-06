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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import tabview.ListViewItem;

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
        db.execSQL("CREATE TABLE IF NOT EXISTS SOJU (" +
                "s_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                "C1 integer default 0,"+
                "GOOD_DAY integer default 0,"+
                "SOON_HARI integer default 0,"+
                "LIKE_FIRST integer default 0,"+
                "MT_HANRA integer default 0"+
                " );");

        db.execSQL("CREATE TABLE IF NOT EXISTS BEER (" +
                "s_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                "CASS integer default 0,"+
                "HITE integer default 0,"+
                "MAX integer default 0,"+
                "OB integer default 0"+
                " );");


        db.execSQL("CREATE TABLE IF NOT EXISTS MAKG (" +
                "s_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                "GUKS integer default 0,"+
                "GEUM integer default 0,"+
                "NEUR integer default 0,"+
                "SAEN integer default 0,"+
                "SEOU integer default 0,"+
                "UGUK integer default 0"+
                " );");

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS CUP (" +
                "s_time TIMESTAMP NOT NULL, " +
                "f_time TIMESTAMP NOT NULL, " +
                "ml_soju integer DEFAULT 0, " +
                "ml_macj integer DEFAULT 0, " +
                "ml_mack integer DEFAULT 0, " +
                "hangover integer DEFAULT 0, " +
                "PRIMARY KEY(s_time));"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS DC (" +
                "ID int NOT NULL UNIQUE, " +
                "user_dc integer DEFAULT 0);"
        );

        db.execSQL("INSERT INTO DC VALUES (1,0);");

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS PHONE (" +
                "phone_num varchar(20) NOT NULL UNIQUE, " +
                "user_name varchar(20) NOT NULL);");

        db.execSQL("CREATE TABLE IF NOT EXISTS POSITION (" +
                "lat varchar(30) UNIQUE NOT NULL," +
                "lng varchar(30) UNIQUE NOT NULL," +
                "isCheck integer DEFAULT 0);");

    }
    // IF NOT EXISTS

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void executeQuery(String _query) {
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

    public boolean checkZero(double target) {
        double epsilon = 0.000001;
        return Math.abs(target) < epsilon;
    }

    public int get_current_DC() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;

        String query = "SELECT user_dc FROM DC";

        cs = db.rawQuery(query,null);
        cs.moveToFirst();

        int ret = cs.getInt(0);
        return ret;
    }

    public ArrayList<ListViewItem> getPhone() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        String query = "SELECT * FROM PHONE";
        ArrayList<ListViewItem> arr = new ArrayList<ListViewItem>();
        cs = db.rawQuery(query,null);

        while(cs.moveToNext()) {
            ListViewItem temp = new ListViewItem();
            temp.setPhone_num(cs.getString(0));
            temp.setUser_name(cs.getString(1));
            arr.add(temp);
        }
        return arr;
    }

    public int getDC() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs,cs_soju,cs_beer,cs_makg;

        String query = "SELECT s_time, ml_soju, ml_macj, ml_mack, hangover FROM CUP";

        cs = db.rawQuery(query,null);

        int record_num = cs.getCount();
        int[][] record = new int[record_num][2];

        int record_count = 0;
        while(cs.moveToNext()) {

            String s_time = cs.getString(0);

            double ml_soju = cs.getInt(1);
            double ml_beer = cs.getInt(2);
            double ml_makg = cs.getInt(3);
            double al_soju = 0, al_beer = 0, al_makg = 0;

            if(cs.getInt(1) > 0){//소주마심
                String query_brand = "SELECT * from SOJU where s_time = '"+ s_time + "'";
                cs_soju = db.rawQuery(query_brand,null);
                cs_soju.moveToFirst();
                if(cs_soju.getCount() == 0){//no feedback
                    al_soju = ml_soju * 0.17;
                }
                else{
                    double sum = cs_soju.getInt(1)+cs_soju.getInt(2) +cs_soju.getInt(3)+cs_soju.getInt(4)+cs_soju.getInt(5);
                    al_soju = ml_soju * cs_soju.getInt(1)/sum  * 0.19  + ml_soju * cs_soju.getInt(2)/sum * 0.169 + ml_soju * cs_soju.getInt(3)/sum * 0.14+ ml_soju * cs_soju.getInt(4)/sum* 0.175 + ml_soju * cs_soju.getInt(5)/sum* 0.21;
                }
            }
            if(cs.getInt(2) > 0){//맥주 마심
                String query_brand = "SELECT * from BEER where s_time = '"+ s_time + "'";
                cs_beer = db.rawQuery(query_brand,null);
                cs_beer.moveToFirst();
                if(cs_beer.getCount() == 0){//no feedback
                    al_beer = ml_beer * 0.045;
                }
                else{
                    double sum = cs_beer.getInt(1) + cs_beer.getInt(2) + cs_beer.getInt(3) + cs_beer.getInt(4);
                    al_beer = ml_beer * cs_beer.getInt(1)/sum  * 0.045  + ml_beer * cs_beer.getInt(2)/sum * 0.045 + ml_beer * cs_beer.getInt(3)/sum * 0.045+ ml_beer * cs_beer.getInt(4)/sum* 0.048;
                }
            }
            if(cs.getInt(3) > 0){//막걸리 마심
                String query_brand = "SELECT * from MAKG where s_time = '"+ s_time + "'";
                cs_makg = db.rawQuery(query_brand,null);
                cs_makg.moveToFirst();
                if(cs_makg.getCount() == 0){//no feedback
                    al_makg = ml_makg * 0.06;
                }
                else{
                    double sum = cs_makg.getInt(1)+cs_makg.getInt(2) +cs_makg.getInt(3)+cs_makg.getInt(4)+cs_makg.getInt(5)+cs_makg.getInt(6);
                    al_makg = ml_makg * cs_makg.getInt(1)/sum  * 0.06  + ml_makg * cs_makg.getInt(2)/sum * 0.08 + ml_makg * cs_makg.getInt(3)/sum * 0.06+ ml_makg * cs_makg.getInt(4)/sum* 0.06 + ml_makg * cs_makg.getInt(5)/sum* 0.06 + ml_makg * cs_makg.getInt(6)/sum* 0.06;
                }
            }
            int hangover_day = cs.getInt(4);
            record[record_count][0] = (int)(al_soju + al_beer + al_makg);
            record[record_count][1] = hangover_day;

            record_count++;

        }
////////////////모든 값 배열에 넣음



        int max = 0;//괜찮았을때중 최대
        int min = 10000;//안괜찮았을떄중 최소
        int sum_all = 0;
        int record_distance = 0;

        for(int i = 0 ; i < record_num ; i++){

            if(record[i][1] == -1){// 안취
                if( max < record[i][0]){
                    max = record[i][0];
                }
                record_distance++;
            }
            else if(record[i][1] == 1){//취
                if(min > record[i][0]){
                    min = record[i][0];
                }
                record_distance++;
            }
            sum_all += record[i][0];
        }
/////////////////////최대, 최소 계산

        int average_all = sum_all/record_num;//이사람 평균적으로 얼마 마시는지


        int dc_distance = 0;

        if(average_all <= 45){// 0.5 내외
            dc_distance = 30;
        }
        else if(average_all <= 75){//1.0 내외
            dc_distance = 60;
        }
        else if(average_all <= 105){//1.5 내외
            dc_distance = 90;
        }
        else if(average_all <= 135){//2.0 내외
            dc_distance = 120;
        }
        else{
            dc_distance = 150;
        }


        int calculated_dc = 0;

        if(record_distance <= 5 || max- min >= dc_distance){// 자료 부족
            Cursor cs_dc;
            String query_dc = "SELECT user_dc FROM DC order by user_dc desc limit 1";
            cs_dc = db.rawQuery(query_dc,null);
            calculated_dc = cs.getInt(0);
        }
        else {
            int[][] record_dc = new int[record_distance][2];

            int dc_i = 0;
            for (int i = 0; i < cs.getCount(); i++) {
                if (record[i][0] >= min && record[i][0] <= max) {

                    record_dc[dc_i][0] = record[i][0];
                    record_dc[dc_i][1] = record[i][1];
                    dc_i++;
                }
            }
            //따로 배열만듬


            for (int i = record_distance; i >= 0; i--) {
                for (int k = 0; k < i - 1; k++) {
                    if (record_dc[k][0] > record_dc[k + 1][0]) {
                        int temp_ml, temp_hangover;
                        temp_ml = record_dc[k + 1][0];
                        temp_hangover = record_dc[k + 1][1];
                        record_dc[k + 1][0] = record_dc[k][0];
                        record_dc[k + 1][1] = record_dc[k][1];

                        record_dc[k][0] = temp_ml;
                        record_dc[k][1] = temp_hangover;
                    }
                }
            }
            ////정렬 끝

            int standard = -1;
            for (int i = 1; i < record_distance; i++) {
                int hangover_yes = 0, hangover_no = 0;
                for (int k = 0; k < i; k++) {
                    if (record_dc[k][1] == 1) {
                        hangover_yes++;
                    }
                }
                for (int k = i; k < record_distance; k++) {
                    if (record_dc[k][1] == -1) {
                        hangover_no++;
                    }
                }
                if (hangover_yes == hangover_no && hangover_yes != 0) {
                    standard = i;
                }
            }
            calculated_dc = record_dc[standard - 1][0];
        }


        return calculated_dc;
    }

    public boolean checkDC(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs_dc;
        String query_dc = "SELECT user_dc FROM DC";
        cs_dc = db.rawQuery(query_dc,null);
        if(cs_dc.getCount() == 0){
            return false;
        }
        return true;
    }

    public String getLatestTime(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        String query = "select s_time from CUP order by s_time desc limit 1";

        cs = db.rawQuery(query, null);
        cs.moveToFirst();

        if(cs.getCount() == 0) {
            return "자료 부족";
        }
        else {
            return cs.getString(0);
        }
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
        if(cs.getCount() == 0){
            return "자료 부족";
        }
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

        if(count_c1 + count_gd + count_sh + count_lf + count_mh == 0){
            return "자료 부족";
        }
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

        if(count_ob + count_hite + count_cass + count_max == 0){
            return "자료 부족";
        }

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
        count_hashmap.put("국산당쌀",count_guks);

        cs = db.rawQuery(query_geum,null);
        count_geum = cs.getCount();
        count_hashmap.put("금정산성",count_geum);


        cs = db.rawQuery(query_neur,null);
        count_neur = cs.getCount();
        count_hashmap.put("느루마을",count_neur);


        cs = db.rawQuery(query_saen,null);
        count_saen = cs.getCount();
        count_hashmap.put("생탁",count_saen);


        cs= db.rawQuery(query_seou,null);
        count_seou = cs.getCount();
        count_hashmap.put("서울장수",count_seou);


        cs= db.rawQuery(query_uguk,null);
        count_uguk = cs.getCount();
        count_hashmap.put("우국생",count_uguk);

        if(count_guks + count_geum + count_neur + count_saen + count_seou + count_uguk == 0){
            return "자료 부족";
        }

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

    public int getAverageDrinkTime(){//리턴이 -1일시 자료부족 아니면 분 리턴.
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        String query_hangover = "select strftime('%H',s_time),strftime('%M',s_time),strftime('%H',f_time),strftime('%M',f_time) from CUP ";
        cs = db.rawQuery(query_hangover, null);

        if( cs.getCount() < 5){
            return -1;
        }

        int time = 0;
        while(cs.moveToNext()){
            int s_hour = cs.getInt(0);
            int s_min = cs.getInt(1);
            int f_hour = cs.getInt(2);
            int f_min = cs.getInt(3);

            if(f_hour < s_hour){
                f_hour += 24;
            }
            int interval = f_hour - s_hour;
            int interval_min = interval * 60;
            time += interval_min - s_min + f_min;
        }
        int average_time = time / cs.getCount();

        return average_time;
    }

    public String getTrend(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        String query_hangover = "select hangover,s_time from CUP where hangover != 0 order by s_time desc  limit 5";
        //최근 5개의 데이터에서 숙취 유무 체크
        cs = db.rawQuery(query_hangover, null);
        int hangover = 0;
        int flag_hangover = -1;
        if(cs.getCount() < 5){
            return "-1";
        }
        else {
            while (cs.moveToNext()) {
                //Log.e("음",""+cs.getInt(0) + " , " +cs.getString(1));
                if (cs.getInt(0) == 1) {
                    hangover++;
                }
            }//hangover >=3 일시 요즘 과하다.
            if(hangover >=3){
                flag_hangover = 1;
            }
        }


        ////////////// 최근 1주일
        Calendar calendar = Calendar.getInstance();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String today_date = CurDateFormat.format(date);
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        String query_week ="";
        query_week = "SELECT ml_soju, ml_macj, ml_mack ,s_time FROM CUP WHERE s_time BETWEEN datetime('" + today_date +"', '-6 days') AND datetime('" + today_date +"', '-0 days');";
        Cursor cs_week;

        cs_week = db.rawQuery(query_week,null);
        int alchol = 0;
        int flag_alchol = -1;
        //Log.e("??","갯수 : " + cs_week.getCount());
        if(cs_week.getCount() == 0){
            //이번주 기록 x
        }
        else{
            while(cs_week.moveToNext()) {
                //Log.e("??","날짜 : " +today_date+ " vs "+ cs_week.getString(3));
                alchol += 0.17 *cs_week.getInt(0) + 0.045 * cs_week.getInt(1) + 0.06 * cs_week.getInt(2);
            }
            if(alchol <122){// 2병이하
                flag_alchol = -1;
            }
            else{
                flag_alchol = 1;
            }
        }
        //Log.e("???"," hangover, alchol =  "+ hangover + ", "+alchol);
        String result = "";
        if(flag_alchol == 1 && flag_hangover == 1){
            result = "요즘 들어 주량보다 더 많은 량의 술을 드시고 계십니다. \n" +
                    "또 최근 1주 동안 의사의 권장량인 2병 보다 더 많은 술을 드셨습니다 :(\n" +
                    "건강에 무리가 갈 수 있으니 음주를 줄이시길 권장합니다.";
        }
        if(flag_alchol == 1 && flag_hangover == -1){
            result = "최근 데이터를 보니 의사들의 권장량 보다는 적게 마시고 계십니다만" +
                    " 본인의 주량보다는 더 많은 량을 드시고 계십니다.\n" +
                    "한번에 조금씩만 덜 마시는건 어떨까요? :)";
        }
        if(flag_alchol == -1 && flag_hangover == 1){
            result = "본인의 주량을 넘지 않는 선에서 적절히 마시고 계십니다.\n" +
                    "하지만 의사분들이 권장하는 량보다는 더 많이 드시고 계십니다.\n" +
                    "술자리를 횟수를 줄여 보시는 건 어떨까요? :)";
        }
        if(flag_alchol == -1 && flag_hangover == -1){
            result = "자주 마시지도 않고 마실때도 자신의 주량을 넘지 않으 시는군요!\n" +
                    "아주 좋은 음주 습관을 가지고 계십니다 :D";
        }

        return result;
    }

    public ArrayList<doublePair> getLocation() {
        ArrayList<doublePair> arr = new ArrayList<doublePair>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        String query_location = "select lat,lng from POSITION where isCheck = 0;";
        cs=db.rawQuery(query_location, null);

        while(cs.moveToNext()) {
            doublePair temp = new doublePair(35.0,129.0);
            temp.setA(Double.parseDouble(cs.getString(0)));
            temp.setB(Double.parseDouble(cs.getString(1)));
            arr.add(temp);
        }
        return arr;
    }

    public int existFeedBack(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs;
        String query_latest = "select s_time from CUP  order by s_time desc  limit 1";
        //최근 5개의 데이터에서 숙취 유무 체크
        cs = db.rawQuery(query_latest, null);
        cs.moveToFirst();
        String s_time = cs.getString(0);

        String query_soju = "select s_time from SOJU where s_time == '"+s_time+"' ";
        String query_beer = "select s_time from BEER where s_time == '"+s_time+"' ";
        String query_makg = "select s_time from MAKG where s_time == '"+s_time+"' ";
        Cursor cs_soju, cs_beer, cs_makg;
        cs_soju = db.rawQuery(query_soju, null);
        cs_beer = db.rawQuery(query_beer, null);
        cs_makg = db.rawQuery(query_makg, null);

        if(cs_soju.getCount() + cs_beer.getCount() +cs_makg.getCount() == 0){
            return -1;
        }
        return 1;
    }

    public void setData(){
        SQLiteDatabase db = getWritableDatabase();
        String[] query_cup = new String[]{
                "insert into CUP values('2016-07-01 20:33:00','2016-07-02 00:25:00',820,0,0,1);",
                "insert into CUP values('2016-07-02 19:40:00','2016-07-02 22:11:00',460,570,0,1);",
                "insert into CUP values('2016-07-05 22:10:00','2016-07-06 00:07:00',540,0,0,1);",
                "insert into CUP values('2016-07-08 21:52:00','2016-07-08 23:52:00',0,1530,0,-1);",
                "insert into CUP values('2016-07-09 22:40:00','2016-07-10 00:48:00',670,0,0,1);",
                "insert into CUP values('2016-07-15 20:20:00','2016-07-15 22:46:00',530,0,0,-1);",
                "insert into CUP values('2016-07-17 19:45:00','2016-07-17 22:31:00',760,220,0,1);",
                "insert into CUP values('2016-07-22 22:33:00','2016-07-23 01:17:00',370,0,0,-1);",
                "insert into CUP values('2016-07-27 21:35:00','2016-07-27 23:59:00',180,480,0,-1);",
                "insert into CUP values('2016-07-29 18:29:00','2016-07-29 22:01:00',650,0,0,-1);",
                "insert into CUP values('2016-08-04 22:31:00','2016-08-05 00:28:00',780,0,0,1);",
                "insert into CUP values('2016-08-05 19:36:00','2016-08-05 21:55:00',560,0,0,1);",
                "insert into CUP values('2016-08-06 21:23:00','2016-08-06 23:40:00',0,0,970,-1);",
                "insert into CUP values('2016-08-13 22:38:00','2016-08-14 00:54:00',520,0,0,-1);",
                "insert into CUP values('2016-08-18 21:35:00','2016-08-18 23:12:00',690,0,0,1);",
                "insert into CUP values('2016-08-20 21:07:00','2016-08-20 23:47:00',0,1260,0,-1);",
                "insert into CUP values('2016-08-21 20:21:00','2016-08-21 23:11:00',480,0,0,-1);",
                "insert into CUP values('2016-08-27 19:30:00','2016-08-27 22:24:00',650,0,0,1);",
                "insert into CUP values('2016-09-03 18:37:00','2016-09-03 22:28:00',770,0,0,1);",
                "insert into CUP values('2016-09-05 19:53:00','2016-09-05 23:56:00',560,0,0,1);",
                "insert into CUP values('2016-09-09 23:20:00','2016-09-10 02:15:00',210,680,0,-1);",
                "insert into CUP values('2016-09-10 21:34:00','2016-09-10 23:38:00',0,0,740,-1);",
                "insert into CUP values('2016-09-18 22:48:00','2016-09-19 00:43:00',730,0,0,1);",
                "insert into CUP values('2016-09-22 21:31:00','2016-09-22 23:20:00',550,0,0,1);",
                "insert into CUP values('2016-09-24 22:47:00','2016-09-25 00:51:00',670,0,0,1);",
                "insert into CUP values('2016-09-30 19:29:00','2016-09-30 22:48:00',420,310,0,-1);",
                "insert into CUP values('2016-10-02 22:30:00','2016-10-03 00:24:00',660,0,0,1);",
                "insert into CUP values('2016-10-06 22:42:00','2016-10-07 01:25:00',0,0,1120,-1);",
                "insert into CUP values('2016-10-08 20:14:00','2016-10-08 23:22:00',340,0,0,-1);",
                "insert into CUP values('2016-10-16 22:22:00','2016-10-17 01:26:00',350,710,0,-1);",
                "insert into CUP values('2016-10-22 20:30:00','2016-10-22 23:58:00',460,0,0,-1);",
                "insert into CUP values('2016-10-23 22:51:00','2016-10-24 00:19:00',580,0,0,1);",
                "insert into CUP values('2016-10-27 21:37:00','2016-10-27 23:21:00',0,0,980,-1);",
                "insert into CUP values('2016-10-29 20:18:00','2016-10-31 00:56:00',410,0,0,-1);",
                "insert into CUP values('2016-11-04 22:30:00','2016-11-05 00:27:00',370,0,0,-1);",
                "insert into CUP values('2016-11-06 20:51:00','2016-11-06 22:39:00',300,360,0,-1);"

        };

        String[] query_soju = new String[]{"insert into SOJU values('2016-07-01 20:33:00',1,3,0,0,0);",
                "insert into SOJU values('2016-07-02 19:40:00',0,1,0,0,0);",
                "insert into SOJU values('2016-07-05 22:10:00',0,1,0,0,0);",
                "insert into SOJU values('2016-07-09 22:40:00',0,1,0,0,0);",
                "insert into SOJU values('2016-07-15 20:20:00',0,1,0,0,0);",
                "insert into SOJU values('2016-07-17 19:45:00',1,0,0,0,0);",
                "insert into SOJU values('2016-07-22 22:33:00',0,1,0,0,0);",
                "insert into SOJU values('2016-07-27 21:35:00',0,1,0,0,0);",
                "insert into SOJU values('2016-07-29 18:29:00',0,1,0,0,0);",
                "insert into SOJU values('2016-08-04 22:31:00',0,2,1,0,0);",
                "insert into SOJU values('2016-08-05 19:36:00',0,1,0,0,0);",
                "insert into SOJU values('2016-08-13 22:38:00',1,0,0,0,0);",
                "insert into SOJU values('2016-08-18 21:35:00',0,1,0,0,0);",
                "insert into SOJU values('2016-08-21 20:21:00',0,1,0,0,0);",
                "insert into SOJU values('2016-08-27 19:30:00',1,4,0,0,0);",
                "insert into SOJU values('2016-08-03 18:37:00',0,1,0,0,0);",
                "insert into SOJU values('2016-08-05 19:53:00',0,1,0,0,0);",
                "insert into SOJU values('2016-08-09 23:20:00',0,1,0,0,0);",
                "insert into SOJU values('2016-08-18 22:48:00',0,3,0,1,0);",
                "insert into SOJU values('2016-08-22 21:31:00',0,1,0,0,0);",
                "insert into SOJU values('2016-08-24 22:47:00',1,0,0,0,0);",
                "insert into SOJU values('2016-08-30 19:29:00',1,0,0,0,0);",
                "insert into SOJU values('2016-09-02 22:30:00',2,1,0,0,0);",
                "insert into SOJU values('2016-09-08 20:14:00',0,1,0,0,0);",
                "insert into SOJU values('2016-09-16 22:22:00',1,0,0,0,0);",
                "insert into SOJU values('2016-09-22 20:30:00',0,1,0,0,0);",
                "insert into SOJU values('2016-09-23 22:51:00',0,2,0,1,0);",
                "insert into SOJU values('2016-09-29 20:18:00',0,1,0,0,0);",
                "insert into SOJU values('2016-11-04 22:30:00',0,1,0,0,0);",
                "insert into SOJU values('2016-11-06 20:51:00',0,1,0,0,0);",
        };

        String[] query_beer= new String[]{
                "insert into BEER values('2016-04-02 19:40:00',0,0,1,0);",
                "insert into BEER values('2016-04-08 21:52:00',0,0,1,0);",
                "insert into BEER values('2016-04-17 19:45:00',1,0,0,0);",
                "insert into BEER values('2016-04-27 21:35:00',0,0,1,0);",
                "insert into BEER values('2016-05-20 21:07:00',1,0,0,0);",
                "insert into BEER values('2016-06-09 23:20:00',0,0,1,0);",
                "insert into BEER values('2016-06-30 19:29:00',0,0,1,0);",
                "insert into BEER values('2016-07-16 22:22:00',0,0,1,0);",
                "insert into BEER values('2016-07-29 20:18:00',1,0,0,0);",
                "insert into BEER values('2016-08-06 20:51:00',0,0,1,0);",
                "insert into BEER values('2016-08-12 20:30:00',0,0,1,0);",
                "insert into BEER values('2016-08-14 23:30:00',0,0,1,0);"
        };
        String[] query_makg= new String[]{
                "insert into MAKG values('2016-05-06 21:23:00',0,0,0,1,0,0);",
                "insert into MAKG values('2016-06-10 21:34:00',0,0,0,1,0,0);",
                "insert into MAKG values('2016-07-06 22:42:00',0,0,0,1,0,0);",
                "insert into MAKG values('2016-07-27 21:37:00',0,0,0,1,0,0);"
        };

        for(int i = 0 ; i < query_cup.length ;i++) {
            db.execSQL(query_cup[i]);
        }
        for(int i = 0 ; i < query_soju.length ;i++) {
            db.execSQL(query_soju[i]);
        }
        for(int i = 0 ; i < query_beer.length ;i++) {
            db.execSQL(query_beer[i]);
        }
        for(int i = 0 ; i < query_makg.length ;i++) {
            db.execSQL(query_makg[i]);
        }
    }




}
