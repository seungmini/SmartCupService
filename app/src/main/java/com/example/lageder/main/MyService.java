package com.example.lageder.main;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;

import datas.AlcoholThread;
import datas.SCSDBManager;
import tabview.ListViewItem;

public class MyService extends Service {
    NotificationManager Notifi_M;
    AlcoholThread thread;
    Notification Notifi ;
    private ICallback mCallback;
    SCSDBManager db;
    GPSTracker gps;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        thread.interrupt();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        db = new SCSDBManager(getApplicationContext(), "s2.db", null, 1);
        thread = new AlcoholThread(db, handler);
        thread.start();
        return START_STICKY;
    }

    public class MyServiceBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    private final IBinder mBinder = new MyServiceBinder();

    public interface ICallback {
        public void sendSMS(String phoneNumber, String message);
    }

    public void registerCallback(ICallback cb) {
        mCallback = cb;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(MyService.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

            Notifi = new Notification.Builder(getApplicationContext())
                    .setContentTitle("주량이 위험합니다.")
                    .setContentText("이 이상 음주를 하면 위험해집니다!")
                    .setSmallIcon(R.drawable.logo)
                    .setTicker("Alert!")
                    .setContentIntent(pendingIntent)
                    .build();

            //소리추가
            Notifi.defaults = Notification.DEFAULT_SOUND;

            //알림 소리를 한번만 내도록
            Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;

            //확인하면 자동으로 알림이 제거 되도록
            Notifi.flags = Notification.FLAG_AUTO_CANCEL;


            Notifi_M.notify( 777 , Notifi);

            ArrayList<ListViewItem> item_list = db.getPhone();


            for(ListViewItem item : item_list)
                mCallback.sendSMS(item.getPhone_num(),"사용자분이 술에 취한 상태입니다. 귀가여부에 신경써주십시오.");
        }
    };
}
