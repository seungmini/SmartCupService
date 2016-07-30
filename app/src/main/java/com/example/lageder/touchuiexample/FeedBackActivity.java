package com.example.lageder.touchuiexample;

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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by LeeSeungMin
 */


public class FeedBackActivity extends AppCompatActivity {

    private ListView mainListView;
    private CustomAdapter listAdapter;
    private TextView feedback_title;
    private Display display;
    private Button button_ok, button_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        mainListView = (ListView) findViewById(R.id.drink_listview);
        listAdapter = new CustomAdapter(this);
        mainListView.setAdapter(listAdapter);

        button_ok = (Button)findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                for(int i = 0 ; i < listAdapter.getCount(); i++){
                    Log.e("술"," " + listAdapter.listview_data.get(i).drink_type);
                    int drink_type = listAdapter.listview_data.get(i).drink_type_int;
                    if(drink_type == 0){
                        String arr[] = getResources().getStringArray(R.array.soju_list);
                        Log.e("타입"," " + arr[listAdapter.getDrinkName(i)]);
                    }
                    else if(drink_type == 1){
                        String arr[] = getResources().getStringArray(R.array.macju_list);
                        Log.e("타입"," " + arr[listAdapter.getDrinkName(i)]);
                    }
                    else {
                        String arr[] = getResources().getStringArray(R.array.macguli_list);
                        Log.e("타입"," " + arr[listAdapter.getDrinkName(i)]);
                    }

                    String arr1[] = getResources().getStringArray(R.array.number_of_drink);
                    Log.e("량"," " + arr1[listAdapter.getDrinkNumber(i)]);
                }

            }
        });
        button_cancel = (Button)findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Log.e("갯수"," " + listAdapter.getCount());

                //finish();
            }
        });


        feedback_title = (TextView) findViewById(R.id.toolbar_title);
        feedback_title.setText("Feed Back");


        display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.7);
        int height = (int) (display.getHeight() * 0.8);
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
                        if(id == 0 || id == 1 || id == 2){
                            listAdapter.add(id);
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
