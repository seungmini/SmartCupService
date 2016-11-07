package datas;


import android.os.Handler;
import android.util.Log;

import com.example.lageder.main.GPSTracker;

/**
 * Created by Lageder on 2016-11-01.
 */

public class AlcoholThread extends Thread {
    SCSDBManager db;
    GPSTracker gps;
    Handler handler;
    Handler notiHandler;
    boolean isRun = true, isOver = false, isSend = false;
    int cur_alcohol, limit;

    public AlcoholThread(GPSTracker gps, SCSDBManager target, Handler handler, Handler notiHandler, boolean isSend) {
        this.handler = handler;
        this.db = target;
        this.notiHandler = notiHandler;
        this.gps = gps;
        this.isSend = isSend;
    }

    public void stopForever() {
        synchronized (this) {
            this.isRun = false;
            this.isOver = false;
        }
    }

    public void run() {
        // 이걸 보내면 바로 알림이 뜬다.
        /*handler.sendEmptyMessage(0);*/
        while(isRun) {
            try{
                limit = db.getDC();
                cur_alcohol = db.get_current_DC();
                double per = cur_alcohol / limit;
                if(per >= 80.0) {
                    isOver = true;

                    if(isSend)
                        handler.sendEmptyMessage(0);

                    notiHandler.sendEmptyMessage(0);
                    break;
                }
                Thread.sleep(600000);
            }catch(Exception e) {}
        }
        while(isOver) {
            try {
                if(gps.canGetLocation()) {
                    Log.e("ABCDEFG","I got gps!");
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Log.e("ABCDEFG","lat = " + latitude + " and lng = " + longitude);
                    gps.stopUsingGPS();
                    String query = "insert into POSITION values(" + latitude + "," + longitude + "," + 0 + ");";
                    db.executeQuery(query);
                }
                Thread.sleep(600000);

            } catch(Exception e) {

            }
        }
    }
}
