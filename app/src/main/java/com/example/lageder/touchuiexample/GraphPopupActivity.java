package com.example.lageder.touchuiexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * Created by LeeSeungMin
 */

public class GraphPopupActivity extends Activity {
    private BarChart chart_popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;

        getWindow().setAttributes(layoutParams);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); title 제거

        setContentView(R.layout.activity_graph_popup);


        //크기 조절
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.8); //Display 사이즈의 80%
        int height = (int) (display.getHeight() * 0.5);  //Display 사이즈의 50%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        Intent intent = getIntent();
        int chart_number = intent.getIntExtra("chart_number",-1);
        //   Log.e("?? : ", Integer.toString(chart_number));
        this.setTitle(Integer.toString(chart_number));
        ////// 차트 추가
        chart_popup = (BarChart)findViewById(R.id.popup_chart);
        makeGraphPopup(chart_number);

    }

    public void makeGraphPopup(int chart_number){
        BarData data = new BarData(getXLabel_week(), getDataSet_week(chart_number));

        chart_popup.setData(data);

        //시작값 0 설정
        YAxis yAxis_left = chart_popup.getAxisLeft();
        YAxis yAxis_right = chart_popup.getAxisRight();
        yAxis_left.setAxisMinValue(0f);
        yAxis_right.setAxisMinValue(0f);

        chart_popup.setTouchEnabled(false);
        chart_popup.setDescription("description");
        chart_popup.animateY(2000);
    }



    public BarDataSet getDataSet_week(int chart_number){
        //값 설정
        ArrayList<BarEntry> alchol_array = new ArrayList<>();
        alchol_array.add(new BarEntry(4f,0));
        alchol_array.add(new BarEntry(1f,1));
        alchol_array.add(new BarEntry(0,2));
        alchol_array.add(new BarEntry(8f,3));
        alchol_array.add(new BarEntry(10f,4));
        alchol_array.add(new BarEntry(0,5));
        alchol_array.add(new BarEntry(3f,6));

        BarDataSet data_set = new BarDataSet(alchol_array,"how much drink");
        data_set.setColors(ColorTemplate.COLORFUL_COLORS);

        return data_set;
    }

    public ArrayList<String> getXLabel_week(){
        ArrayList<String> x_labels = new ArrayList<String>();
        x_labels.add("MON");
        x_labels.add("TUE");
        x_labels.add("WEN");
        x_labels.add("TUS");
        x_labels.add("FRI");
        x_labels.add("SAT");
        x_labels.add("SUN");
        return x_labels;
    }

}