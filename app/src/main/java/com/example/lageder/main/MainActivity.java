package com.example.lageder.main;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import bluetoothLe.SampleGattAttributes;
import datas.SCSDBManager;
import kakaolinkage.KakaoLoginActivity;
import tabview.DrinkPopupActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity  extends AppCompatActivity{
    private final static String TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSION_REQUEST_STORAGE = 1;
    private static final int REQUEST_SERVICE = 1;

    private BluetoothLeService mBluetoothLeService;
    private MyService myService;

    private GPSTracker gps;
    private String start_time;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private BackPressCloseHandler backPressCloseHandler;
    private TextView title_textview;
    private String mDeviceName;
    private String mDeviceAddress;

    private boolean mConnected = false;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private ImageView drink_imageview;
    private int total_al_value;
    private String al_kind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        total_al_value = 0;

        String name_path=getFilesDir(). getAbsolutePath()+"/name.txt";
        File name_file = new File(name_path);
        //getAppKeyHash();

        //카톡 연동 체크
        String temp_name = "사용자";
        if(name_file.exists() != true){
            Intent graph_intent = new Intent(getApplicationContext(), KakaoLoginActivity.class);
            startActivity(graph_intent);
        }
        else{
            try {
                FileInputStream fis = new FileInputStream(name_file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                temp_name = new String(buffer);

                fis.close();
            } catch (IOException e) {
                Log.e("File", "에러=" + e);
            }
        }

        final String name = temp_name;
        //뒤로가기 두번 = 종료 설정
        backPressCloseHandler = new BackPressCloseHandler(this);

        title_textview = (TextView)findViewById(R.id.main_title);
        Typeface font_gabia = Typeface.createFromAsset(this.getAssets(), "gabia_solmee.ttf");
        title_textview.setTypeface(font_gabia);

        drink_imageview = (ImageView)findViewById(R.id.drink_image);
        drink_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent drink_popup_intent = new Intent(getApplicationContext(), DrinkPopupActivity.class);
                drink_popup_intent.putExtra("name",name);
                startActivity(drink_popup_intent);
            }
        });

        //기본 툴바, 레이아웃, 뷰페이저 설정
        //toolbar = (Toolbar) findViewById(R.id.tool_bar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        String db_path = getDatabasePath("s1.db").getAbsolutePath();
        File db = new File(db_path);
        if(db.exists() != true){
            SCSDBManager db_manager = new SCSDBManager(this, "s2.db", null, 1);
            db_manager.setData();
        }


        //탭 추가
        final TabLayout.Tab profile_tab = tabLayout.newTab();
        final TabLayout.Tab graph_tab = tabLayout.newTab();
        final TabLayout.Tab setting_tab = tabLayout.newTab();

        addTap(profile_tab,R.drawable.profile_32,"프로필", 0);
        addTap(graph_tab,R.drawable.graph_32,"최근 경향", 1);
        addTap(setting_tab,R.drawable.settings_32,"환경설정", 2);


        tabLayout.setTabTextColors(Color.rgb(255,255,255),Color.rgb(255,255,255));
        tabLayout.setSelectedTabIndicatorColor(Color.rgb(255,255,255));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab){}

            @Override
            public void onTabReselected(TabLayout.Tab tab){}
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }

    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }


    public void addTap(TabLayout.Tab tab,int tap_icon, String tap_text,int count){
        View view = getLayoutInflater().inflate(R.layout.custom_tab,null);

        TextView text = (TextView) view.findViewById(R.id.tab_text);
        text.setText(tap_text);

        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        icon.setImageResource(tap_icon);

        Typeface font_gabia = Typeface.createFromAsset(getAssets(), "gabia_solmee.ttf");
        text.setTypeface(font_gabia);

        tab.setCustomView(view);
        tabLayout.addTab(tab,count);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("ActivityResult","Popup has been completed");
        Log.e("ActivityResult","RequestCode = " + requestCode);
        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SERVICE) {
                Log.e("ActivityResult","and result is OK");
                mDeviceName = data.getStringExtra(EXTRAS_DEVICE_NAME);
                Log.e("ActivityResult","Device name:" + mDeviceName);
                mDeviceAddress = data.getStringExtra(EXTRAS_DEVICE_ADDRESS);

/*                if (mDeviceName.equals("FBL770 v2.0.0")) {
                    mDeviceName = " BLE - UART";
                    Log.e("ActivityResult","and you changed BLE - UART");
                }*/
                Log.e("ActivityResult","and you called Intent");
                Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
                boolean result = bindService(gattServiceIntent,mServiceConnection,BIND_AUTO_CREATE);
                Log.e("ActivityResult","Device address:" + mDeviceAddress.toString());
                if(result) {
                    Log.e("ActivityResult","I think, bind works.");
                }
            }
        }
        else if(resultCode == RESULT_CANCELED) {
            if(requestCode == 1) {
                Log.e("ActivityResult","Activity result is Canceled");
                mDeviceName = data.getStringExtra(EXTRAS_DEVICE_NAME);
                Log.e("ActivityResult","Device name:" + mDeviceName);
                mDeviceAddress = data.getStringExtra(EXTRAS_DEVICE_ADDRESS);
                Log.e("ActivityResult","Device address:" + mDeviceAddress.toString());
            }
        }
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            Log.e("ActivityResult","I am copying");
            mBluetoothLeService.connect(mDeviceAddress);
            //Log.e("ActivityResult","Work done with " + mBluetoothLeService.connect(mDeviceAddress));
            Toast.makeText(getApplicationContext(), "Connect!", Toast.LENGTH_SHORT).show();
            //Log.d(TAG, "Connect request result=" + mBluetoothLeService.connect(mDeviceAddress));
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("ActivityResult","It was null");
            mBluetoothLeService = null;
        }
    };

    private MyService.ICallback mCallback = new MyService.ICallback() {
        public void sendSMS(String phoneNumber, String message) {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SENT), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(DELIVERED), 0);
            // when the SMS has been sent
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch(getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "믿을만한 사람에게 전송되었습니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));
            SmsManager sms = SmsManager.getDefault();
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS},1);
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }
    };

    private ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.MyServiceBinder binder = (MyService.MyServiceBinder) iBinder;
            myService = binder.getService();
            myService.registerCallback(mCallback);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myService = null;
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mConnected) {
            unbindService(mServiceConnection);
            mBluetoothLeService = null;
            Log.e("RA","Disconnect completed");
        }
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                long time = System.currentTimeMillis();

                Intent service = new Intent(MainActivity.this, MyService.class);
                bindService(service, myServiceConnection, Context.BIND_AUTO_CREATE);

                SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                start_time = dayTime.format(new Date(time));

                SCSDBManager sj_manager = new SCSDBManager(getApplicationContext(), "s2.db", null, 1);
                String query_update = "UPDATE DC SET user_dc = " + 0;
                sj_manager.executeQuery(query_update);

/*                Intent service_intent = new Intent(MainActivity.this,MyService.class);
                startService(service_intent);*/

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                Log.d(TAG, "======= Disconnected ");

/*                Intent service_intent = new Intent(MainActivity.this,MyService.class);
                stopService(service_intent);*/

                long time = System.currentTimeMillis();
                SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String end_time = dayTime.format(new Date(time));
                SCSDBManager sj_manager = new SCSDBManager(getApplicationContext(), "s2.db", null, 1);
                if(start_time != "") {
                    if(al_kind.compareTo("S") == 0) {
                        String query = "insert into CUP values ('" + start_time + "','" + end_time + "'," + total_al_value + "," + 0 + "," + 0 + "," + 0 + ")";
                        sj_manager.executeQuery(query);
                    }
                    else if(al_kind.compareTo("B") == 0) {
                        String query = "insert into CUP values ('" + start_time + "','" + end_time + "'," + 0 + "," + total_al_value + "," + 0 + "," + 0 + ")";
                        sj_manager.executeQuery(query);
                    }
                    else if(al_kind.compareTo("M") == 0) {
                        String query = "insert into CUP values ('" + start_time + "','" + end_time + "'," + 0 + "," + 0 + "," + total_al_value + "," + 0 + ")";
                        sj_manager.executeQuery(query);
                    }
                    start_time = "";
                }

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                getDeviceSetting();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                byte[] sendByte = intent.getByteArrayExtra("init");
                if ((sendByte[0] == 0x55) && (sendByte[1] == 0x33)) {
                    Log.d(TAG, "======= Init Setting Data ");

                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // notification enable
                            final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(3).get(1);
                            mBluetoothLeService.setCharacteristicNotification(characteristic, true);
                        }

                    }, 1000);
                }

                if ((sendByte[0] == 0x55) && (sendByte[1] == 0x03)) {
                    Log.d(TAG, "======= SPP READ NOTIFY ");

                    String s = "";
                    s += intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                    Log.e("Lageder",s + "is the value");

                    String al_value = "";
                    String[] data = s.split(" ");
                    al_kind = "";
                    for(int i = 0;i<data.length;i++) {
                        //Log.e("Lageder","data[" + i + "] : " + data[i]);
                        int decimal = Integer.parseInt(data[i], 16);
                        if(i == 0) {
                            if(decimal >= 'A' && decimal <= 'Z') {
                                al_kind += (char)decimal;
                                Log.e("Lageder","data[" + i + "] : " + al_kind);
                            }
                            else
                                al_kind += 'S';
                        }
                        else {
                            if(decimal >= '0' && decimal <= '9') {
                                al_value += (char)decimal;
                            }
                            else al_value += '0';
                        }
                    }
                    total_al_value += Integer.parseInt(al_value);
                    SCSDBManager sj_manager = new SCSDBManager(getApplicationContext(), "s2.db", null, 1);
                    String query_update = "UPDATE DC SET user_dc = " + total_al_value;

                }
            }
        }
    };



    public void disconnect_ble() {
        unbindService(mServiceConnection);
        unbindService(myServiceConnection);
        mBluetoothLeService = null;
        myService = null;
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();

            Log.d(TAG, "service uuid : " + uuid);

            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                Log.d(TAG, "gattCharacteristic uuid : " + uuid);


                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
       /* SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 },
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        mGattServicesList.setAdapter(gattServiceAdapter); */
        Log.d(TAG, "service read ok ");
    }

    private void getDeviceSetting() {
        if (mGattCharacteristics != null) {
            // error occurs. What is characteristic>
            final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(6).get(0);
            mBluetoothLeService.readCharacteristic(characteristic);
        }
    }

    public void checkPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Explain to the user why we need to write the permission.
                //Toast.makeText(this, "Access Fine Location and Access Coarse Location", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_REQUEST_STORAGE);

            //Toast.makeText(getApplicationContext(), "권한이 인가되었습니다.", Toast.LENGTH_LONG).show();
            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
            // Do my method
            do_gps();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! do the
                    // calendar task you need to do.

                    // Do my method
                    do_gps();
                    Log.d("RACommunication", "Permission always be granted");

                } else {

                    Log.d("RACommunication", "Permission always deny");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }

    private void do_gps() {
        gps = new GPSTracker(MainActivity.this);
        // check if GPS enabled
        if(gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String cityName = addresses.get(0).getAdminArea();
            if(cityName.compareTo("부산광역시") == 0) {
                Toast.makeText(getApplicationContext(), cityName + "의 추천 술은 C1입니다.", Toast.LENGTH_LONG).show();
            }
            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \n City name: " + cityName, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            // gps.showSettingsAlert();
        }
    }

/*    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
        // when the SMS has been sent
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "믿을만한 사람에게 전송되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
        SmsManager sms = SmsManager.getDefault();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }*/

}
