package com.example.lageder.touchuiexample;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lageder.touchuiexample.MainActivity;

/**
 * Created by LeeSeungMin
 */


public class FeedBackActivity extends Activity {

    private ListView mainListView;
    private CustomAdapter listAdapter;
    private TextView feedback_title;
    private Display display;
    private Button button_ok, button_cancel;
    SeekBar seekbar_hangover;
    TextView textview_hangover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        mainListView = (ListView) findViewById(R.id.drink_listview);
        listAdapter = new CustomAdapter(this);
        mainListView.setAdapter(listAdapter);


        button_cancel = (Button) findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Log.e("갯수", " " + listAdapter.getCount());
                SojuDBManager sj_manager = new SojuDBManager(getApplicationContext(), "Test_Soju.db", null, 1);
                sj_manager.PrintData();
                //finish();
            }
        });

        seekbar_hangover = (SeekBar) findViewById(R.id.seekBar);
        textview_hangover = (TextView) findViewById(R.id.hangover_textview);

        seekbar_hangover.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Log.e("흠", "" +mSeekBar.getProgress());
                textview_hangover.setText("Value = " + progress);
            }
        });


        button_ok = (Button) findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < listAdapter.getCount(); i++) {

                    //Log.e("술"," " + listAdapter.listview_data.get(i).drink_type);
                    String drink = listAdapter.listview_data.get(i).drink_type;
                    int drink_type = listAdapter.listview_data.get(i).drink_type_int;

                    String brand;
                    if (drink_type == 0) {
                        String arr[] = getResources().getStringArray(R.array.soju_list);
                        brand = arr[listAdapter.getDrinkName(i)];
                        SojuDBManager sj_manager = new SojuDBManager(getApplicationContext(), "Test_Soju.db", null, 1);
                        if (brand.equals("C1")) {
                            Log.e("DB", "Trying to set db value");
                            sj_manager.insert("insert into SOJU (C1) values (1);");
                            Log.e("DB", "Setting has been done");
                        } else if (brand.equals("좋은데이")) {
                            Log.e("DB", "Trying to set db value");
                            sj_manager.insert("insert into SOJU (GOOD_DAY) values (1);");
                            Log.e("DB", "Setting has been done");
                        } else if (brand.equals("순하리")) {
                            Log.e("DB", "Trying to set db value");
                            sj_manager.insert("insert into SOJU (SOON_HARI) values (1);");
                            Log.e("DB", "Setting has been done");
                        } else if (brand.equals("처음처럼")) {
                            Log.e("DB", "Trying to set db value");
                            sj_manager.insert("insert into SOJU (LIKE_FIRST) values (1);");
                            Log.e("DB", "Setting has been done");
                        } else if (brand.equals("한라산")) {
                            Log.e("DB", "Trying to set db value");
                            sj_manager.insert("insert into SOJU (MT_HANRA) values (1);");
                            Log.e("DB", "Setting has been done");
                        }

                        /*
                        * select id,sqltime,
                        sum(SOJU.C1) C1_SUM,
                        sum(SOJU.GOOD_DAY) GOOD_DAY_SUM,
                        sum(SOJU.SOON_HARI) SOON_HARI_SUM,
                        sum(SOJU.LIKE_FIRST) LIKE_FIRST_SUM,
                        sum(SOJU.MT_HANRA) MT_SUM
                        from SOJU group by sqltime;
                        * */

                        /*select
                        sum(SOJU.C1) C1,
                                sum(SOJU.GOOD_DAY) GOOD_DAY,
                                sum(SOJU.LIKE_FIRST) LIKE_FIRST,
                                sum(SOJU.SOON_HARI) SOON_HARI,
                                sum(SOJU.MT_HANRA) MT_HANRA
                        from SOJU
                        where (SOJU.sqltime > '2016-07-05 00:00:00')
                        and (SOJU.sqltime < '2016-08-05 00:00:00');*/

                    } else if (drink_type == 1) {
                        String arr[] = getResources().getStringArray(R.array.macju_list);
                        brand = arr[listAdapter.getDrinkName(i)];
                        // Log.e("타입"," " + arr[listAdapter.getDrinkName(i)]);
                    } else {
                        String arr[] = getResources().getStringArray(R.array.macguli_list);
                        brand = arr[listAdapter.getDrinkName(i)];
                        //Log.e("타입"," " + arr[listAdapter.getDrinkName(i)]);
                    }

                    String arr1[] = getResources().getStringArray(R.array.number_of_drink);
                    int drink_count = Integer.parseInt(arr1[listAdapter.getDrinkNumber(i)]);
                    // Log.e("량"," " + arr1[listAdapter.getDrinkNumber(i)]);

                    Log.e("" + (i + 1), "종류 : " + drink + ", Brand : " + brand + ", 마신 량 :" + drink_count + "잔");
                }

                Log.e(" ", "숙취 정도 : " + seekbar_hangover.getProgress());
            }
        });

        display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.8);
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

    public void showDialog() {
        final CharSequence[] drink_type = {"소주", "맥주", "막걸리"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // 제목셋팅
        alertDialogBuilder.setTitle("주류 목록");
        alertDialogBuilder.setItems(drink_type, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // 프로그램을 종료한다
                Toast.makeText(getApplicationContext(), drink_type[id] + " 선택했습니다.", Toast.LENGTH_SHORT).show();
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
