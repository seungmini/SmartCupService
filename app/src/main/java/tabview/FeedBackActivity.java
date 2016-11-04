package tabview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lageder.main.R;

import datas.SCSDBManager;

/**
 * Created by LeeSeungMin
 */


public class FeedBackActivity extends Activity {

    private ListView mainListView;
    private CustomAdapter listAdapter;
    private TextView feedback_title;
    private Display display;
    private Button button_ok, button_cancel;
    CheckBox button_hangover;
    SeekBar seekbar_hangover;
    TextView textview_detail_3,textview_detail_1,textview_detail_2,textview_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        mainListView = (ListView) findViewById(R.id.drink_listview);
        listAdapter = new CustomAdapter(this);
        mainListView.setAdapter(listAdapter);

        textview_title = (TextView) findViewById(R.id.feeadback_title);
        textview_detail_1 = (TextView) findViewById(R.id.detail_textview1);
        textview_detail_2 = (TextView) findViewById(R.id.detail_textview2);
        textview_detail_3 = (TextView) findViewById(R.id.detail_textview3);


        Typeface font_gabia = Typeface.createFromAsset(this.getAssets(), "gabia_solmee.ttf");

        textview_title.setTypeface(font_gabia);
        textview_detail_1.setTypeface(font_gabia);
        textview_detail_2.setTypeface(font_gabia);
        textview_detail_3.setTypeface(font_gabia);

        button_hangover=(CheckBox) findViewById(R.id.hangover_checkbox);



        button_cancel = (Button) findViewById(R.id.button_cancel);
        button_cancel.setTypeface(font_gabia);
        button_cancel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Log.e("갯수", " " + listAdapter.getCount());
                //SCSDBManager sj_manager = new SCSDBManager(getApplicationContext(), "Sample.db", null, 1);
                finish();
            }
        });

        button_ok = (Button) findViewById(R.id.button_ok);
        button_ok.setTypeface(font_gabia);
        button_ok.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                clickOk();
                /*SCSDBManager sj_manager = new SCSDBManager(getApplicationContext(), "abcde.db", null, 1);
                String latest_time = sj_manager.getLatestTime();

                int soju_c1 = 0, soju_gd = 0, soju_sh = 0, soju_lf = 0, soju_hl = 0;
                int beer_cass = 0, beer_hite = 0, beer_max = 0, beer_ob = 0;
                int mack_guks = 0, mack_geum = 0, mack_neur = 0, mack_saen = 0, mack_seou = 0 , mack_uguk = 0;
                int soju_exist = -1, beer_exist = -1, mack_exist = -1;

                if(latest_time != "0") {
                    //sj_manager.executeQuery("insert into SOJU(time),BEER(time),MAKG(time)  values " + latest_time + ";");
                    for (int i = 0; i < listAdapter.getCount(); i++) {

                        String drink = listAdapter.listview_data.get(i).drink_type;
                        int drink_type = listAdapter.listview_data.get(i).drink_type_int;
                        String brand;

                        String arr1[] = getResources().getStringArray(R.array.number_of_drink);
                        int drink_count = Integer.parseInt(arr1[listAdapter.getDrinkNumber(i)]);

                        if (drink_type == 0) {//소주
                            String arr[] = getResources().getStringArray(R.array.soju_list);
                            brand = arr[listAdapter.getDrinkName(i)];

                            if (brand.equals("C1")) {
                                soju_c1 += drink_count;
                            } else if (brand.equals("좋은데이")) {
                                soju_gd += drink_count;
                            } else if (brand.equals("순하리")) {
                                soju_sh += drink_count;
                            } else if (brand.equals("처음처럼")) {
                                soju_lf += drink_count;
                            } else if (brand.equals("한라산")) {
                                soju_hl += drink_count;
                            }

                            soju_exist = 1;

                        } else if (drink_type == 1) {
                            String arr[] = getResources().getStringArray(R.array.beer_list);
                            brand = arr[listAdapter.getDrinkName(i)];

                            if (brand.equals("Cass")) {
                                beer_cass += drink_count;
                            } else if (brand.equals("Hite")) {
                                beer_hite += drink_count;
                            } else if (brand.equals("MAX")) {
                                beer_max += drink_count;
                            } else if (brand.equals("OB")) {
                                beer_ob += drink_count;
                            }

                            beer_exist = 1;

                        } else {
                            String arr[] = getResources().getStringArray(R.array.makgeolli_list);
                            brand = arr[listAdapter.getDrinkName(i)];

                            if (brand.equals("국순당쌀")) {
                                mack_guks += drink_count;
                            } else if (brand.equals("금정산성")) {
                                mack_geum += drink_count;
                            } else if (brand.equals("느린마을")) {
                                mack_neur += drink_count;
                            } else if (brand.equals("생탁")) {
                                mack_saen += drink_count;
                            } else if (brand.equals("서울장수")) {
                                mack_seou += drink_count;
                            } else if (brand.equals("우국생")) {
                                mack_uguk += drink_count;
                            }

                            mack_exist = 1;

                        }
                    }

                    int hangover = -1;
                    if (button_hangover.isChecked()) {//주량초과
                        hangover = 1;
                    }
                    /*
                    Log.e(" ", "숙취 유무 : " + hangover);
                    Log.e(" ", "날짜 : " + latest_time);
                    Log.e(" ", soju_c1 +" "+ soju_gd +" "+  soju_sh +" "+  soju_lf +" "+  soju_hl );
                    Log.e(" ", beer_cass +" "+  beer_hite +" "+  beer_max +" "+  beer_ob );
                    Log.e(" ", mack_guks +" "+  mack_geum +" "+  mack_neur +" "+  mack_saen +" "+  mack_seou +" "+  mack_uguk );

                    sj_manager.executeQuery("update CUP set hangover = "+hangover+" where s_time = '"+latest_time+"' ");// 숙취 유무 추가

                    if(soju_exist == 1){
                        sj_manager.executeQuery("insert into SOJU  values ('"+latest_time+"',"+soju_c1+","+soju_gd+","+soju_sh+","+soju_lf+","+soju_hl+" ) ;");
                    }
                    if(beer_exist == 1){
                        sj_manager.executeQuery("insert into BEER  values ('"+latest_time+"',"+beer_cass+","+beer_hite+","+beer_max+","+beer_ob+" ) ;");
                    }
                    if(mack_exist == 1){
                        sj_manager.executeQuery("insert into MAKG  values ('"+latest_time+"',"+mack_guks+","+mack_geum+","+mack_neur+","+mack_saen+","+mack_seou+","+mack_uguk+" ) ;");
                    }

                }
                else{
                    Log.e("", "CUP table is null");
                }
                finish();*/
            }
        });

        display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.9);
        int height = (int) (display.getHeight() * 0.9);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        ImageButton add_button = (ImageButton) findViewById(R.id.add_button);
        add_button.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void clickOk(){
        SCSDBManager sj_manager = new SCSDBManager(getApplicationContext(), "s2.db", null, 1);
        String latest_time = sj_manager.getLatestTime();

        int soju_c1 = 0, soju_gd = 0, soju_sh = 0, soju_lf = 0, soju_hl = 0;
        int beer_cass = 0, beer_hite = 0, beer_max = 0, beer_ob = 0;
        int mack_guks = 0, mack_geum = 0, mack_neur = 0, mack_saen = 0, mack_seou = 0 , mack_uguk = 0;
        int soju_exist = -1, beer_exist = -1, mack_exist = -1;

        if(latest_time != "0" && listAdapter.getCount() > 0) {
            //sj_manager.executeQuery("insert into SOJU(time),BEER(time),MAKG(time)  values " + latest_time + ";");
            for (int i = 0; i < listAdapter.getCount(); i++) {

                String drink = listAdapter.listview_data.get(i).drink_type;
                int drink_type = listAdapter.listview_data.get(i).drink_type_int;
                String brand;

                String arr1[] = getResources().getStringArray(R.array.number_of_drink);
                int drink_count = Integer.parseInt(arr1[listAdapter.getDrinkNumber(i)]);

                if (drink_type == 0) {//소주
                    String arr[] = getResources().getStringArray(R.array.soju_list);
                    brand = arr[listAdapter.getDrinkName(i)];

                    if (brand.equals("C1")) {
                        soju_c1 += drink_count;
                    } else if (brand.equals("좋은데이")) {
                        soju_gd += drink_count;
                    } else if (brand.equals("순하리")) {
                        soju_sh += drink_count;
                    } else if (brand.equals("처음처럼")) {
                        soju_lf += drink_count;
                    } else if (brand.equals("한라산")) {
                        soju_hl += drink_count;
                    }

                    soju_exist = 1;

                } else if (drink_type == 1) {
                    String arr[] = getResources().getStringArray(R.array.beer_list);
                    brand = arr[listAdapter.getDrinkName(i)];

                    if (brand.equals("Cass")) {
                        beer_cass += drink_count;
                    } else if (brand.equals("Hite")) {
                        beer_hite += drink_count;
                    } else if (brand.equals("MAX")) {
                        beer_max += drink_count;
                    } else if (brand.equals("OB")) {
                        beer_ob += drink_count;
                    }

                    beer_exist = 1;

                } else {
                    String arr[] = getResources().getStringArray(R.array.makgeolli_list);
                    brand = arr[listAdapter.getDrinkName(i)];

                    if (brand.equals("국순당쌀")) {
                        mack_guks += drink_count;
                    } else if (brand.equals("금정산성")) {
                        mack_geum += drink_count;
                    } else if (brand.equals("느린마을")) {
                        mack_neur += drink_count;
                    } else if (brand.equals("생탁")) {
                        mack_saen += drink_count;
                    } else if (brand.equals("서울장수")) {
                        mack_seou += drink_count;
                    } else if (brand.equals("우국생")) {
                        mack_uguk += drink_count;
                    }

                    mack_exist = 1;

                }
            }

            int hangover = -1;
            if (button_hangover.isChecked()) {//주량초과
                hangover = 1;
            }
                    /*
                    Log.e(" ", "숙취 유무 : " + hangover);
                    Log.e(" ", "날짜 : " + latest_time);
                    Log.e(" ", soju_c1 +" "+ soju_gd +" "+  soju_sh +" "+  soju_lf +" "+  soju_hl );
                    Log.e(" ", beer_cass +" "+  beer_hite +" "+  beer_max +" "+  beer_ob );
                    Log.e(" ", mack_guks +" "+  mack_geum +" "+  mack_neur +" "+  mack_saen +" "+  mack_seou +" "+  mack_uguk );
                    */
            sj_manager.executeQuery("update CUP set hangover = "+hangover+" where s_time = '"+latest_time+"' ");// 숙취 유무 추가

            if(soju_exist == 1){
                sj_manager.executeQuery("insert into SOJU  values ('"+latest_time+"',"+soju_c1+","+soju_gd+","+soju_sh+","+soju_lf+","+soju_hl+" ) ;");
            }
            if(beer_exist == 1){
                sj_manager.executeQuery("insert into BEER  values ('"+latest_time+"',"+beer_cass+","+beer_hite+","+beer_max+","+beer_ob+" ) ;");
            }
            if(mack_exist == 1){
                sj_manager.executeQuery("insert into MAKG  values ('"+latest_time+"',"+mack_guks+","+mack_geum+","+mack_neur+","+mack_saen+","+mack_seou+","+mack_uguk+" ) ;");
            }

        }
        else{
            Log.e("", "CUP table is null Or List is null");
        }
        finish();
    }

    public void showDialog() {
        final CharSequence[] drink_type = {"소주", "맥주", "막걸리"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // 제목셋팅
        alertDialogBuilder.setTitle("주류 목록");

        alertDialogBuilder.setItems(drink_type, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // 프로그램을 종료한다
                //Toast.makeText(getApplicationContext(), drink_type[id] + " 선택했습니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if (id == 0 || id == 1 || id == 2) {
                    listAdapter.add(id);
                    listAdapter.notifyDataSetChanged();
                }
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
