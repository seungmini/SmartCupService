package com.example.lageder.touchuiexample;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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


/**
 * Created by LeeSeungMin
 */

public class GraphActivity extends AppCompatActivity{


    private BarChart chart;
    private Toolbar toolbar;
    private Spinner spinner_date;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //툴바 설정////////////////////////////////////
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        //툴바 설정////////////////////////////////////


        spinner_date = (Spinner)findViewById(R.id.date_spinner);
        makeSpinnerList();
        //spinner_date.setOnItemClickListener();
        spinner_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(arg2 == 0){
                    makeWeekGraph();
                }
                else{
                    makeMonthGraph();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        ////// 차트 추가
        chart = (BarChart)findViewById(R.id.chart);
        makeWeekGraph();

    }

    public void makeSpinnerList(){
        ArrayList<String> datelist = new ArrayList<>();
        datelist.add("최근 한주 GRAPH");
        datelist.add("최근 한달 GRAPH");
        ArrayAdapter<String> list = new ArrayAdapter<String>(this,R.layout.spinner_item,datelist);
        spinner_date.setAdapter(list);
    }
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.drawer_open,R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.nav_first_fragment) {
                        //    Intent graph_intent = new Intent(getApplicationContext(), GraphActivity.class);
                         //   startActivity(graph_intent);
                        } else if (id == R.id.nav_second_fragment) {

                        } else if (id == R.id.nav_third_fragment) {
                            Intent feedback_intent = new Intent(getApplicationContext(), FeedBackActivity.class);
                            startActivity(feedback_intent);
                        }
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

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
                        Intent graph_popup_intent = new Intent(getApplicationContext(), GraphPopupActivity.class);
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
