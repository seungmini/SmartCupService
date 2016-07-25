package com.example.lageder.touchuiexample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;


public class FeedBackActivity extends AppCompatActivity {

    private ListView mainListView ;
    private CustomAdapter listAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);



        mainListView = (ListView) findViewById( R.id.drink_listview );
        listAdapter = new CustomAdapter();
        mainListView.setAdapter( listAdapter );

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.7);
        int height = (int) (display.getHeight() * 0.8);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;


        this.setTitle("???????");

        ImageButton add_button = (ImageButton)findViewById(R.id.add_button);
        add_button.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v){
                listAdapter.add();
                listAdapter.notifyDataSetChanged();
            }

        });
    }


}
