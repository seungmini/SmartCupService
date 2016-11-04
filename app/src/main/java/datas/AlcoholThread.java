package datas;


import android.os.Handler;

/**
 * Created by Lageder on 2016-11-01.
 */

public class AlcoholThread extends Thread {
    SCSDBManager db;
    Handler handler;
    boolean isRun = true, isOver = false;
    int cur_alcohol, limit;

    public AlcoholThread(SCSDBManager target, Handler handler) {
        this.handler = handler;
        this.db = target;
    }

    public void stopForever() {
        synchronized (this) {
            this.isRun = false;
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
                    handler.sendEmptyMessage(0);
                    break;
                }
                Thread.sleep(60000);
            }catch(Exception e) {}
        }
        while(isOver) {;}
    }
}
