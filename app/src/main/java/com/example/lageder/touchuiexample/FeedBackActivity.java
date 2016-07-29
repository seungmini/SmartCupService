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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        mainListView = (ListView) findViewById(R.id.drink_listview);
        listAdapter = new CustomAdapter(this);
        mainListView.setAdapter(listAdapter);

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
