package com.example.lageder.touchuiexample;

/**
 * Created by Lageder on 2016-07-23.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphActivity extends Fragment {
    Activity activity;
    private BarChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_graph, container, false);

        activity = getActivity();

        chart = (BarChart)v.findViewById(R.id.chart);
        makeWeekGraph();

        return v;
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()){
            case R.id.item_week:
                makeWeekGraph();
                return true;
            case R.id.item_month:
                makeMonthGraph();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
*/

    public BarDataSet getDataSet_week(){
        //값 설정
        ArrayList<BarEntry> alchol_array = new ArrayList<>();
        alchol_array.add(new BarEntry(4f,0));
        alchol_array.add(new BarEntry(0,1));
        alchol_array.add(new BarEntry(0,2));
        alchol_array.add(new BarEntry(8f,3));
        alchol_array.add(new BarEntry(10f,4));
        alchol_array.add(new BarEntry(0,5));
        alchol_array.add(new BarEntry(0,6));

        BarDataSet data_set = new BarDataSet(alchol_array,"how much drink");
        data_set.setColors(ColorTemplate.COLORFUL_COLORS);

        return data_set;
    }
    public BarDataSet getDataSet_month(){
        //값 설정
        ArrayList<BarEntry> alchol_array = new ArrayList<>();
        alchol_array.add(new BarEntry(4f,0));
        alchol_array.add(new BarEntry(0,1));
        alchol_array.add(new BarEntry(0,2));
        alchol_array.add(new BarEntry(8f,3));

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

    public ArrayList<String> getXLabel_month(){
        ArrayList<String> x_labels = new ArrayList<String>();
        x_labels.add("3 week ago");
        x_labels.add("2 week ago");
        x_labels.add("1 week ago");
        x_labels.add("this week");
        return x_labels;
    }
    public void makeWeekGraph(){

        BarData data = new BarData(getXLabel_week(), getDataSet_week());

        chart.setData(data);

        //시작값 0 설정
        YAxis yAxis_left = chart.getAxisLeft();
        YAxis yAxis_right = chart.getAxisRight();
        yAxis_left.setAxisMinValue(0f);
        yAxis_right.setAxisMinValue(0f);

        chart.setTouchEnabled(false);
        chart.setDescription("description");
        chart.animateY(2000);
    }
    public void makeMonthGraph(){

        BarData data = new BarData(getXLabel_month(), getDataSet_month());
        chart.setData(data);
        chart.setTouchEnabled(true);

        //touch 빼고 다 끔
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDragDecelerationEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        //////////////////


        //시작값 0 설정
        YAxis yAxis_left = chart.getAxisLeft();
        YAxis yAxis_right = chart.getAxisRight();
        yAxis_left.setAxisMinValue(0f);
        yAxis_right.setAxisMinValue(0f);


        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                switch(e.getXIndex()){
                    case 0:
                        Intent graph_popup_intent = new Intent(getActivity().getApplicationContext(), GraphPopupActivity.class);
                        graph_popup_intent.putExtra("chart_number",0);
                        startActivity(graph_popup_intent);
                        break;
                    case 1:
                        Log.d("Tag","Select Second Bar");
                        break;
                    case 2:
                        Log.d("Tag","Select Third Bar");
                        break;
                    case 3:
                        Log.d("Tag","Select Forth Bar");
                        break;

                }

            }

            @Override
            public void onNothingSelected() {
                // do nothing
            }
        });
        chart.setDescription("description");
        chart.animateY(2000);
    }

}
